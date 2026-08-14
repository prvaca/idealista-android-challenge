package com.paloma.idealista.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil.load
import com.paloma.idealista.R
import com.paloma.idealista.databinding.FragmentAdDetailBinding
import com.paloma.idealista.domain.model.AdDetailModel
import com.paloma.idealista.ui.common.components.HeaderView
import com.paloma.idealista.ui.common.UiState
import com.paloma.idealista.ui.common.components.AdDetailSkeleton
import com.paloma.idealista.util.AppConstants.EMPTY_STRING
import com.paloma.idealista.util.AppConstants.SPANISH_LOCALE
import com.paloma.idealista.util.DateFormatter
import com.paloma.idealista.util.OperationType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.NumberFormat

@AndroidEntryPoint
class AdDetailFragment : Fragment() {

    private var _binding: FragmentAdDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AdDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUiState()

        binding.skeletonComposeView.setContent {
            AdDetailSkeleton()
        }

        binding.headerComposeView.setContent {
            HeaderView (
                title = getString(R.string.header_detail_title),
                showBackAction = true,
                onBackClick = { findNavController().navigateUp() }
            )
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.skeletonComposeView.visibility = View.GONE
                    binding.textError.visibility = View.GONE
                    binding.scrollContent.visibility = View.GONE

                    when (state) {
                        is UiState.Loading -> binding.skeletonComposeView.visibility = View.VISIBLE
                        is UiState.Success -> {
                            binding.scrollContent.visibility = View.VISIBLE
                            bindDetail(state.data)
                        }
                        is UiState.Error -> {
                            binding.textError.visibility = View.VISIBLE
                            binding.textError.text = state.message ?: getString(R.string.detail_error_unknown)
                        }
                    }
                }
            }
        }
    }

    private fun bindDetail(detail: AdDetailModel) {
        binding.imageMain.load(detail.imageUrls.firstOrNull()) {
            placeholder(R.drawable.ic_placeholder_image)
            error(R.drawable.ic_placeholder_error)
        }
        binding.textOperation.text = when (detail.operation) {
            OperationType.RENT -> getString(R.string.operation_rent)
            else -> getString(R.string.operation_sale)
        }

        val formattedPrice = NumberFormat.getNumberInstance(SPANISH_LOCALE)
            .format(detail.price.toInt())
        binding.textPrice.text = getString(R.string.ad_price_formatted, formattedPrice, detail.currencySuffix)
        binding.textRooms.text = detail.rooms?.let { getString(R.string.ad_rooms_count, it) } ?: EMPTY_STRING
        binding.textBathrooms.text = detail.bathrooms?.let { getString(R.string.ad_bathrooms_count, it) } ?: EMPTY_STRING
        binding.textArea.text = detail.constructedArea?.let { getString(R.string.ad_area, it.toInt()) } ?: EMPTY_STRING
        binding.textFloor.text = detail.floor?.let { getString(R.string.ad_floor, it) } ?: EMPTY_STRING

        binding.buttonFavorite.setImageResource(
            if (detail.isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border
        )

        val favoritedAt = detail.favoritedAt
        if (detail.isFavorite && favoritedAt != null) {
            binding.dividerFavorite.visibility = View.VISIBLE
            binding.textFavoritedDate.visibility = View.VISIBLE
            binding.textFavoritedDate.text = getString(
                R.string.ad_favorited_since,
                DateFormatter.formatFavoritedDate(favoritedAt)
            )
        } else {
            binding.dividerFavorite.visibility = View.GONE
            binding.textFavoritedDate.visibility = View.GONE
        }

        binding.textDescription.text = detail.description

        binding.buttonFavorite.setOnClickListener { viewModel.toggleFavorite() }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}