package com.paloma.idealista.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.paloma.idealista.databinding.ItemAdBinding
import com.paloma.idealista.domain.model.Ad

class AdsListAdapter(
    private val onItemClick: (Ad) -> Unit,
    private val onFavoriteClick: (Ad) -> Unit
) : ListAdapter<Ad, AdViewHolder>(AdDiffCallback()) {

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

private class AdDiffCallback : DiffUtil.ItemCallback<Ad>() {
    override fun areItemsTheSame(oldItem: Ad, newItem: Ad) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Ad, newItem: Ad) = oldItem == newItem
}