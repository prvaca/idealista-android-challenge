package com.paloma.idealista.ui.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.paloma.idealista.R
import com.paloma.idealista.databinding.ItemAdBinding
import com.paloma.idealista.domain.model.AdModel
import com.paloma.idealista.util.AppConstants
import com.paloma.idealista.util.DateFormatter
import com.paloma.idealista.util.OperationType
import java.text.NumberFormat

class AdViewHolder(
    private val binding: ItemAdBinding,
    private val onItemClick: (AdModel) -> Unit,
    private val onFavoriteClick: (AdModel) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(ad: AdModel) {
        binding.imageThumbnail.load(ad.thumbnailUrl) {
            placeholder(R.drawable.ic_placeholder_image)
            error(R.drawable.ic_placeholder_error)
        }

        val formattedPrice = NumberFormat.getNumberInstance(AppConstants.SPANISH_LOCALE)
            .format(ad.price.toInt())
        binding.textPrice.text = binding.root.context.getString(
            R.string.ad_price_formatted, formattedPrice, ad.currencySuffix
        )

        binding.textOperation.text = when (ad.operation) {
            OperationType.RENT -> binding.root.context.getString(R.string.operation_rent)
            else -> binding.root.context.getString(R.string.operation_sale)
        }

        binding.textAddress.text = ad.address ?: ad.neighborhood ?: ad.district

        binding.textRooms.text = binding.root.context.getString(R.string.ad_rooms_count, ad.rooms)
        binding.textBathrooms.text = binding.root.context.getString(R.string.ad_bathrooms_count, ad.bathrooms)
        binding.textSize.text = binding.root.context.getString(R.string.ad_area, ad.size.toInt())

        binding.buttonFavorite.setImageResource(
            if (ad.isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border
        )

        val favoritedAt = ad.favoritedAt
        if (ad.isFavorite && favoritedAt != null) {
            binding.dividerFavorite.visibility = View.VISIBLE
            binding.textFavoritedDate.visibility = View.VISIBLE
            binding.textFavoritedDate.text = binding.root.context.getString(
                R.string.ad_favorited_since,
                DateFormatter.formatFavoritedDate(favoritedAt)
            )
        } else {
            binding.dividerFavorite.visibility = View.GONE
            binding.textFavoritedDate.visibility = View.GONE
        }

        binding.root.setOnClickListener { onItemClick(ad) }
        binding.buttonFavorite.setOnClickListener { onFavoriteClick(ad) }
    }
}