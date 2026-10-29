package com.example.mallnav.ui.home

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mallnav.R
import com.example.mallnav.data.model.Mall
import com.example.mallnav.databinding.ItemMallCardBinding

class MallAdapter(
    private val onMallClick: (Mall) -> Unit,
) : ListAdapter<Mall, MallAdapter.MallViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MallViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MallViewHolder(ItemMallCardBinding.inflate(inflater, parent, false), onMallClick)
    }

    override fun onBindViewHolder(holder: MallViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MallViewHolder(
        private val binding: ItemMallCardBinding,
        private val onMallClick: (Mall) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(mall: Mall) {
            val context = binding.root.context
            val accent = ContextCompat.getColor(context, mall.accentColor)
            val container = ContextCompat.getColor(context, mall.accentContainer)
            binding.tvMallName.text = mall.name
            binding.tvMallCity.text = mall.city
            binding.tvStoreCount.text = context.getString(R.string.home_store_count, mall.storeCount)
            binding.accentBar.setBackgroundColor(accent)
            binding.ivMallImage.backgroundTintList = ColorStateList.valueOf(container)
            binding.ivMallImage.setColorFilter(accent)
            binding.root.contentDescription = context.getString(R.string.cd_open_mall, mall.name, mall.city)
            binding.root.setOnClickListener { onMallClick(mall) }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Mall>() {
        override fun areItemsTheSame(oldItem: Mall, newItem: Mall): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Mall, newItem: Mall): Boolean = oldItem == newItem
    }
}
