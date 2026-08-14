package com.paloma.idealista.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.paloma.idealista.databinding.FragmentAdsListBinding
import com.paloma.idealista.domain.model.AdModel
import com.paloma.idealista.ui.common.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.paloma.idealista.ui.common.components.AdSkeletonList
import com.paloma.idealista.ui.common.components.HeaderView
import com.paloma.idealista.R


@AndroidEntryPoint
class AdsListFragment : Fragment() {

    private var _binding: FragmentAdsListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AdsListViewModel by viewModels()

    private lateinit var adapter: AdsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = AdsListAdapter(
            onItemClick = ::onAdClicked,
            onFavoriteClick = { ad -> viewModel.toggleFavorite(ad.id) }
        )
        binding.recyclerAds.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@AdsListFragment.adapter
        }

        binding.skeletonComposeView.setContent {
            AdSkeletonList()
        }

        binding.headerComposeView.setContent {
            val searchQuery by viewModel.searchQuery.collectAsState()
            val showFavoritesOnly by viewModel.showFavoritesOnly.collectAsState()
            val sortOrder by viewModel.sortOrder.collectAsState()
            HeaderView(
                showSearch = true,
                searchQuery = searchQuery,
                onSearchQueryChanged = { viewModel.onSearchQueryChanged(it) },
                showFavoritesFilter = true,
                isFavoritesFilterActive = showFavoritesOnly,
                onToggleFavoritesFilter = { viewModel.toggleFavoritesFilter() },
                showSortAction = true,
                sortOrder = sortOrder,
                onToggleSort = { viewModel.toggleSortOrder() }
            )
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadAds()
        }

        observeUiState()
    }

    private fun onAdClicked(adModel: AdModel) {
        val action = AdsListFragmentDirections
            .actionAdsListFragmentToAdDetailFragment(adModel.id)
        findNavController().navigate(action)
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.swipeRefresh.isRefreshing = false
                    binding.skeletonComposeView.visibility = View.GONE
                    binding.textError.visibility = View.GONE
                    binding.textEmpty.visibility = View.GONE
                    binding.recyclerAds.visibility = View.GONE

                    when (state) {
                        is UiState.Loading -> binding.skeletonComposeView.visibility = View.VISIBLE
                        is UiState.Success -> {
                            if (state.data.isEmpty()) {
                                binding.textEmpty.visibility = View.VISIBLE
                            } else {
                                binding.recyclerAds.visibility = View.VISIBLE
                                adapter.submitList(state.data) {
                                    binding.recyclerAds.scrollToPosition(0)
                                }
                            }
                        }
                        is UiState.Error -> {
                            binding.textError.visibility = View.VISIBLE
                            binding.textError.text = state.message ?:
                            getString(R.string.ads_list_error_unknown)
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

    override fun onResume() {
        super.onResume()
        viewModel.loadAds()
    }
}