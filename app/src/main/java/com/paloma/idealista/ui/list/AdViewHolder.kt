package com.paloma.idealista.ui.list

import android.view.View
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
        binding.imageThumbnail.load(ad.thumbnailUrl) {
            placeholder(R.drawable.ic_placeholder_image)
            error(R.drawable.ic_placeholder_image)
        }

        val formattedPrice = NumberFormat.getNumberInstance(Locale("es", "ES"))
            .format(ad.price.toInt())
        binding.textPrice.text = "$formattedPrice ${ad.currencySuffix}"

        binding.textOperation.text = when (ad.operation) {
            "rent" -> binding.root.context.getString(R.string.operation_rent)
            else -> binding.root.context.getString(R.string.operation_sale)
        }

        binding.textAddress.text = ad.address ?: ad.neighborhood ?: ad.district

        binding.textRooms.text = ad.rooms.toString()
        binding.textBathrooms.text = ad.bathrooms.toString()
        binding.textSize.text = "${ad.size.toInt()} m²"

        binding.buttonFavorite.setImageResource(
            if (ad.isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border
        )

        val showFavoriteInfo = ad.isFavorite && ad.favoritedAt != null
        binding.dividerFavorite.visibility = if (showFavoriteInfo) View.VISIBLE else View.GONE
        binding.textFavoritedDate.visibility = if (showFavoriteInfo) View.VISIBLE else View.GONE
        if (showFavoriteInfo) {
            binding.textFavoritedDate.text =
                "Favorito desde ${DateFormatter.formatFavoritedDate(ad.favoritedAt!!)}"
        }

        binding.root.setOnClickListener { onItemClick(ad) }
        binding.buttonFavorite.setOnClickListener { onFavoriteClick(ad) }
    }
}