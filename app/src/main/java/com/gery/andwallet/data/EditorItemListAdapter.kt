package com.gery.andwallet.data

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gery.andwallet.MainActivity
import com.gery.andwallet.R
import com.gery.andwallet.databinding.EditorItemLayoutBinding
import kotlin.math.absoluteValue

class EditorItemListAdapter(private val context: Context) : ListAdapter<EditorItem, EditorItemListAdapter.EditorItemViewHolder>(DiffCallback()) {

    interface OnBalanceChangedListener {
        fun onBalanceChanged(balance: Int)
    }

    private lateinit var balanceChangedListener: OnBalanceChangedListener

    var items : MutableList<EditorItem> = mutableListOf()

    var balance = 0

    init {
        val thread = Thread {
            val database: EditorItemDatabase = EditorItemDatabase.getInstance((context as MainActivity))
            val items = database.editorItemDao().getAllItems()
            context.runOnUiThread {
                balanceChangedListener = context

                this.items = items
                notifyItemRangeChanged(0, this.items.size)
                for (item in this.items) {
                    balance += item.amount
                }
                balanceChangedListener.onBalanceChanged(balance)
            }
        }
        thread.start()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EditorItemViewHolder {

        val binding = EditorItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EditorItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EditorItemViewHolder, position: Int) {
        holder.bind(items[holder.adapterPosition])

        holder.binding.btnEditorItemDelete.setOnClickListener {
            val thread = Thread {
                val database: EditorItemDatabase = EditorItemDatabase.getInstance((context as MainActivity))
                database.editorItemDao().delete(items[holder.adapterPosition])
                context.runOnUiThread {
                    deleteItem(holder.adapterPosition)
                }
            }
            thread.start()
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<EditorItem>() {
        override fun areItemsTheSame(oldItem: EditorItem, newItem: EditorItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: EditorItem, newItem: EditorItem): Boolean {
            return oldItem == newItem
        }
    }

    fun addItem(item: EditorItem) {
        items.add(item)
        notifyItemInserted(items.lastIndex)

        balance += item.amount
        balanceChangedListener.onBalanceChanged(balance)
    }

    private fun deleteItem(position: Int) {
        balance -= items[position].amount
        balanceChangedListener.onBalanceChanged(balance)

        items.removeAt(position)
        notifyItemRemoved(position)
    }

    fun deleteAll() {
        balance = 0
        balanceChangedListener.onBalanceChanged(balance)

        val size = items.size
        items.clear()
        notifyItemRangeRemoved(0, size)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class EditorItemViewHolder(val binding: EditorItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: EditorItem) {
            binding.tvEditorItemNameValue.text = item.name
            binding.tvEditorItemAmountValue.text = item.amount.absoluteValue.toString()
            if (item.amount < 0) {
                binding.ivEditorItemTypeIcon.setImageResource(R.drawable.money_off_24dp_fill0_wght400_grad0_opsz24)
            } else {
                binding.ivEditorItemTypeIcon.setImageResource(R.drawable.attach_money_24dp_fill0_wght400_grad0_opsz24)
            }
        }
    }

}