package com.paloma.idealista.ui.list

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.paloma.idealista.databinding.ItemAdBinding
import com.paloma.idealista.domain.model.Ad
import java.text.NumberFormat
import java.util.Locale

class AdViewHolder(
    private val binding: ItemAdBinding,
    private val onItemClick: (Ad) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(ad: Ad) {
        binding.imageThumbnail.load(ad.thumbnailUrl)

        val formattedPrice = NumberFormat.getNumberInstance(Locale("es", "ES"))
            .format(ad.price.toInt())
        binding.textPrice.text = "$formattedPrice ${ad.currencySuffix}"

        binding.textAddress.text = ad.address ?: ad.neighborhood ?: ad.district
        binding.textDetails.text = "${ad.rooms} hab · ${ad.bathrooms} baños · ${ad.size.toInt()} m²"

        binding.root.setOnClickListener { onItemClick(ad) }
    }
}