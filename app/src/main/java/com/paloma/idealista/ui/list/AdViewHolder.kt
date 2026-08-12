package com.paloma.idealista.ui.list

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.paloma.idealista.R
import com.paloma.idealista.databinding.ItemAdBinding
import com.paloma.idealista.domain.model.Ad
import com.paloma.idealista.util.DateFormatter
import java.text.NumberFormat
import java.util.Locale

class AdViewHolder(
    private val binding: ItemAdBinding,
    private val onItemClick: (Ad) -> Unit,
    private val onFavoriteClick: (Ad) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(ad: Ad) {
        binding.imageThumbnail.load(ad.thumbnailUrl)

        val formattedPrice = NumberFormat.getNumberInstance(Locale("es", "ES"))
            .format(ad.price.toInt())
        binding.textPrice.text = "$formattedPrice ${ad.currencySuffix}"

        binding.textAddress.text = ad.address ?: ad.neighborhood ?: ad.district
        binding.textDetails.text = "${ad.rooms} hab · ${ad.bathrooms} baños · ${ad.size.toInt()} m²"

        binding.buttonFavorite.setImageResource(
            if (ad.isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border
        )

        if (ad.isFavorite && ad.favoritedAt != null) {
            binding.textFavoritedDate.visibility = android.view.View.VISIBLE
            binding.textFavoritedDate.text = "Favorito desde ${DateFormatter.formatFavoritedDate(ad.favoritedAt)}"
        } else {
            binding.textFavoritedDate.visibility = android.view.View.GONE
        }

        binding.root.setOnClickListener { onItemClick(ad) }
        binding.buttonFavorite.setOnClickListener { onFavoriteClick(ad) }
    }
}