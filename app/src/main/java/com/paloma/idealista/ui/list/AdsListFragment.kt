package com.paloma.idealista.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.paloma.idealista.databinding.FragmentAdsListBinding
import com.paloma.idealista.domain.model.Ad
import com.paloma.idealista.ui.common.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import androidx.navigation.fragment.findNavController

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

        observeUiState()
    }

    private fun onAdClicked(ad: Ad) {
        val action = AdsListFragmentDirections
            .actionAdsListFragmentToAdDetailFragment(ad.id)
        findNavController().navigate(action)
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.progressBar.visibility = View.GONE
                    binding.textError.visibility = View.GONE
                    binding.recyclerAds.visibility = View.GONE

                    when (state) {
                        is UiState.Loading -> binding.progressBar.visibility = View.VISIBLE
                        is UiState.Success -> {
                            binding.recyclerAds.visibility = View.VISIBLE
                            adapter.submitList(state.data)
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

    override fun onResume() {
        super.onResume()
        viewModel.loadAds()
    }
}