package com.gery.andwallet.data

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gery.andwallet.R
import com.gery.andwallet.databinding.EditorItemLayoutBinding
import kotlin.math.absoluteValue

class EditorItemListAdapter : ListAdapter<EditorItem, EditorItemListAdapter.EditorItemViewHolder>(DiffCallback()) {

    interface OnBalanceChangedListener {
        fun onBalanceChanged(balance: Int)
    }

    private lateinit var balanceChangedListener: OnBalanceChangedListener

    private var items = mutableListOf<EditorItem>(
        EditorItem(1, "Title 1", 3000000),
        EditorItem(2, "Title 2", -2000000),
        EditorItem(3, "Title 3", 4000000))

    var balance = 5000000

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EditorItemViewHolder {

        balanceChangedListener = parent.context as OnBalanceChangedListener

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
            deleteItem(holder.adapterPosition)
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

    fun deleteItem(position: Int) {
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