package com.gery.andwallet.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gery.andwallet.R
import com.gery.andwallet.databinding.EditorItemLayoutBinding
import kotlin.math.absoluteValue

class EditorItemListAdapter : ListAdapter<EditorItem, EditorItemListAdapter.EditorItemViewHolder>(DiffCallback()) {

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
        val item = getItem(position)
        holder.bind(item)
    }

    class DiffCallback : DiffUtil.ItemCallback<EditorItem>() {
        override fun areItemsTheSame(oldItem: EditorItem, newItem: EditorItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: EditorItem, newItem: EditorItem): Boolean {
            return oldItem == newItem
        }
    }

    class EditorItemViewHolder(private val binding: EditorItemLayoutBinding) :
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