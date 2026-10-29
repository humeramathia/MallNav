package com.example.mallnav.ui.store

import android.content.res.ColorStateList
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mallnav.R
import com.example.mallnav.data.model.Store
import com.example.mallnav.databinding.ItemStoreCardBinding

class StoreAdapter(
    private val onStoreClick: (Store) -> Unit,
) : ListAdapter<Store, StoreAdapter.Holder>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        return Holder(ItemStoreCardBinding.inflate(inflater, parent, false), onStoreClick)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(getItem(position))

    class Holder(
        private val binding: ItemStoreCardBinding,
        private val onStoreClick: (Store) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(store: Store) {
            val color = ContextCompat.getColor(binding.root.context, categoryColor(store.category))
            binding.tvStoreName.text = store.name
            binding.tvStoreCategory.text = store.category
            binding.tvStoreFloor.text = store.floor
            binding.categoryDot.backgroundTintList = ColorStateList.valueOf(color)
            binding.root.contentDescription = binding.root.context.getString(R.string.cd_open_store, store.name)
            binding.root.setOnClickListener { onStoreClick(store) }
        }
    }

    class SpaceDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            if (parent.getChildAdapterPosition(view) > 0) outRect.top = spacing
        }
    }

    private object Diff : DiffUtil.ItemCallback<Store>() {
        override fun areItemsTheSame(oldItem: Store, newItem: Store) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Store, newItem: Store) = oldItem == newItem
    }

    companion object {
        fun categoryColor(category: String): Int = when (category) {
            "Fashion" -> R.color.hero_blush
            "Food" -> R.color.sand
            "Beauty" -> R.color.terracotta
            "Electronics" -> R.color.sage
            "Sports" -> R.color.hero_olive
            else -> R.color.stone_soft
        }
    }
}
