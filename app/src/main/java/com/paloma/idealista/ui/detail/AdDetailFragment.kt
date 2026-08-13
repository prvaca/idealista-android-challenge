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
import com.paloma.idealista.domain.model.AdDetail
import com.paloma.idealista.ui.common.HeaderView
import com.paloma.idealista.ui.common.UiState
import com.paloma.idealista.util.DateFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

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

        binding.headerComposeView.setContent {
            HeaderView (
                title = "Ad detail",
                showBackAction = true,
                onBackClick = { findNavController().navigateUp() }
            )
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.progressBar.visibility = View.GONE
                    binding.textError.visibility = View.GONE
                    binding.scrollContent.visibility = View.GONE

                    when (state) {
                        is UiState.Loading -> binding.progressBar.visibility = View.VISIBLE
                        is UiState.Success -> {
                            binding.scrollContent.visibility = View.VISIBLE
                            bindDetail(state.data)
                        }
                        is UiState.Error -> {
                            binding.textError.visibility = View.VISIBLE
                            binding.textError.text = state.message
                        }
                    }
                }
            }
        }
    }

    private fun bindDetail(detail: AdDetail) {
        binding.imageMain.load(detail.imageUrls.firstOrNull()) {
            placeholder(R.drawable.ic_placeholder_image)
            error(R.drawable.ic_placeholder_image)
        }
        binding.textOperation.text = when (detail.operation) {
            "rent" -> getString(R.string.operation_rent)
            else -> getString(R.string.operation_sale)
        }

        val formattedPrice = NumberFormat.getNumberInstance(Locale("es", "ES"))
            .format(detail.price.toInt())
        binding.textPrice.text = "$formattedPrice ${detail.currencySuffix}"

        binding.textRooms.text = detail.rooms?.let { "$it hab" } ?: ""
        binding.textBathrooms.text = detail.bathrooms?.let { "$it baños" } ?: ""
        binding.textArea.text = detail.constructedArea?.let { "${it.toInt()} m²" } ?: ""
        binding.textFloor.text = detail.floor?.let { "Planta $it" } ?: ""

        binding.buttonFavorite.setImageResource(
            if (detail.isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border
        )

        val showFavoriteInfo = detail.isFavorite && detail.favoritedAt != null
        binding.dividerFavorite.visibility = if (showFavoriteInfo) View.VISIBLE else View.GONE
        binding.textFavoritedDate.visibility = if (showFavoriteInfo) View.VISIBLE else View.GONE
        if (showFavoriteInfo) {
            binding.textFavoritedDate.text =
                "Favorito desde ${DateFormatter.formatFavoritedDate(detail.favoritedAt!!)}"
        }

        binding.textDescription.text = detail.description

        binding.buttonFavorite.setOnClickListener { viewModel.toggleFavorite() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}