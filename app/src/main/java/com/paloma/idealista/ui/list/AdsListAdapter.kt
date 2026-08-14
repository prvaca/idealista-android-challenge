package com.paloma.idealista.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.paloma.idealista.databinding.ItemAdBinding
import com.paloma.idealista.domain.model.AdModel

class AdsListAdapter(
    private val onItemClick: (AdModel) -> Unit,
    private val onFavoriteClick: (AdModel) -> Unit
) : ListAdapter<AdModel, AdViewHolder>(AdDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdViewHolder {
        val binding = ItemAdBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AdViewHolder(binding, onItemClick, onFavoriteClick)
    }

    override fun onBindViewHolder(holder: AdViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private class AdDiffCallback : DiffUtil.ItemCallback<AdModel>() {
    override fun areItemsTheSame(oldItem: AdModel, newItem: AdModel) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: AdModel, newItem: AdModel) = oldItem == newItem
}