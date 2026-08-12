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
import coil.load
import com.paloma.idealista.databinding.FragmentAdDetailBinding
import com.paloma.idealista.domain.model.AdDetail
import com.paloma.idealista.ui.common.UiState
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
        binding.imageMain.load(detail.imageUrls.firstOrNull())

        val formattedPrice = NumberFormat.getNumberInstance(Locale("es", "ES"))
            .format(detail.price.toInt())
        binding.textPrice.text = "$formattedPrice ${detail.currencySuffix}"

        val roomsText = detail.rooms?.let { "$it hab" }
        val bathroomsText = detail.bathrooms?.let { "$it baños" }
        val areaText = detail.constructedArea?.let { "${it.toInt()} m²" }
        val floorText = detail.floor?.let { "Planta $it" }

        binding.textDetails.text = listOfNotNull(roomsText, bathroomsText, areaText, floorText)
            .joinToString(" · ")

        binding.textDescription.text = detail.description
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}