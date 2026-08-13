package com.paloma.idealista.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.paloma.idealista.databinding.FragmentFavoritesBinding
import com.paloma.idealista.ui.common.UiState
import com.paloma.idealista.ui.common.AdSkeletonList
import com.paloma.idealista.ui.common.HeaderView
import com.paloma.idealista.ui.list.AdsListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoritesViewModel by viewModels()

    private lateinit var adapter: AdsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = AdsListAdapter(
            onItemClick = { /* opcional: navegar a detalle también desde aquí */ },
            onFavoriteClick = { ad -> viewModel.toggleFavorite(ad.id) }
        )
        binding.recyclerFavorites.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@FavoritesFragment.adapter
        }

        binding.skeletonComposeView.setContent {
            AdSkeletonList(itemCount = 2)
        }

        binding.headerComposeView.setContent {
            HeaderView (
                title = "Favorites",
                showBackAction = true,
                onBackClick = { findNavController().navigateUp() }
            )
        }

        observeUiState()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadFavorites()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.skeletonComposeView.visibility = View.GONE
                    binding.textError.visibility = View.GONE
                    binding.textEmpty.visibility = View.GONE
                    binding.recyclerFavorites.visibility = View.GONE

                    when (state) {
                        is UiState.Loading -> binding.skeletonComposeView.visibility = View.VISIBLE
                        is UiState.Success -> {
                            if (state.data.isEmpty()) {
                                binding.textEmpty.visibility = View.VISIBLE
                            } else {
                                binding.recyclerFavorites.visibility = View.VISIBLE
                                adapter.submitList(state.data)
                            }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}