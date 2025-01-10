package com.example.budgetmanager

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetmanager.Tables.Item
import com.example.budgetmanager.databinding.AddItemLayoutBinding
import com.example.budgetmanager.databinding.ItemLayoutBinding
import com.bumptech.glide.Glide

class ItemAdapter(val items: List<Item>, val callBack: ItemListener)
    : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    interface ItemListener{
        fun onItemClicked(index: Int)
        fun onItemLongClick(index: Int)
    }

    inner class ItemViewHolder(private val binding: ItemLayoutBinding)
        : RecyclerView.ViewHolder(binding.root), View.OnClickListener, View.OnLongClickListener {

        init {
            binding.root.setOnClickListener(this)
            binding.root.setOnLongClickListener(this)
        }

        override fun onClick(p0: View?) {
            callBack.onItemClicked(adapterPosition)
        }

        override fun onLongClick(p0: View?): Boolean {
            callBack.onItemLongClick(adapterPosition)
            return true
        }

        fun bind(item: Item) {
            binding.itemTitle.text = item.description
            binding.itemAmount.text = item.amount.toString()
            binding.itemDate.text = item.date
            if (item.photo != null) {
                Glide.with(binding.root.context)
                    .load(Uri.parse(item.photo)) // Load the image from the URI
                    .placeholder(R.drawable.ic_launcher_background) // Default image while loading
                    .circleCrop()
                    .into(binding.itemImage)
            } else {
                Glide.with(binding.root.context)
                    .load(R.drawable.ic_launcher_background)
                    .circleCrop()// Load the default image
                    .into(binding.itemImage)// תמונת ברירת מחדל
            }

            val strokColor = if (item.isExpense) {
                ContextCompat.getColor(binding.root.context, R.color.red)
            } else {
                ContextCompat.getColor(binding.root.context, R.color.green)
            }
            binding.itemCardView.strokeColor = strokColor
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun itemAt(position: Int): Item = items[position]

}