package com.paloma.idealista.ui.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.paloma.idealista.R
import com.paloma.idealista.databinding.ItemAdBinding
import com.paloma.idealista.domain.model.AdModel
import com.paloma.idealista.util.DateFormatter
import java.text.NumberFormat
import java.util.Locale

class AdViewHolder(
    private val binding: ItemAdBinding,
    private val onItemClick: (AdModel) -> Unit,
    private val onFavoriteClick: (AdModel) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(adModel: AdModel) {
        binding.imageThumbnail.load(adModel.thumbnailUrl) {
            placeholder(R.drawable.ic_placeholder_image)
            error(R.drawable.ic_placeholder_image)
        }

        val formattedPrice = NumberFormat.getNumberInstance(Locale("es", "ES"))
            .format(adModel.price.toInt())
        binding.textPrice.text = "$formattedPrice ${adModel.currencySuffix}"

        binding.textOperation.text = when (adModel.operation) {
            "rent" -> binding.root.context.getString(R.string.operation_rent)
            else -> binding.root.context.getString(R.string.operation_sale)
        }

        binding.textAddress.text = adModel.address ?: adModel.neighborhood ?: adModel.district

        binding.textRooms.text = adModel.rooms.toString()
        binding.textBathrooms.text = adModel.bathrooms.toString()
        binding.textSize.text = "${adModel.size.toInt()} m²"

        binding.buttonFavorite.setImageResource(
            if (adModel.isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border
        )

        val showFavoriteInfo = adModel.isFavorite && adModel.favoritedAt != null
        binding.dividerFavorite.visibility = if (showFavoriteInfo) View.VISIBLE else View.GONE
        binding.textFavoritedDate.visibility = if (showFavoriteInfo) View.VISIBLE else View.GONE
        if (showFavoriteInfo) {
            binding.textFavoritedDate.text =
                "Favorito desde ${DateFormatter.formatFavoritedDate(adModel.favoritedAt!!)}"
        }

        binding.root.setOnClickListener { onItemClick(adModel) }
        binding.buttonFavorite.setOnClickListener { onFavoriteClick(adModel) }
    }
}