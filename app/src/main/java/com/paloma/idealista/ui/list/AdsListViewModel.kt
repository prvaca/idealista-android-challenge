package com.paloma.idealista.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paloma.idealista.domain.model.Ad
import com.paloma.idealista.domain.repository.AdsRepository
import com.paloma.idealista.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdsListViewModel @Inject constructor(
    private val repository: AdsRepository
) : ViewModel() {

    private val _allAdsState = MutableStateFlow<UiState<List<Ad>>>(UiState.Loading)
    private val _searchQuery = MutableStateFlow("")

    private val _uiState = MutableStateFlow<UiState<List<Ad>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Ad>>> = _uiState.asStateFlow()

    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        loadAds()

        combine(_allAdsState, _searchQuery) { state, query ->
            when (state) {
                is UiState.Success -> {
                    val filtered = if (query.isBlank()) {
                        state.data
                    } else {
                        state.data.filter { ad ->
                            listOfNotNull(ad.address, ad.neighborhood, ad.district)
                                .any { it.contains(query, ignoreCase = true) }
                        }
                    }
                    UiState.Success(filtered)
                }
                else -> state
            }
        }.onEach { _uiState.value = it }
            .launchIn(viewModelScope)
    }

    fun loadAds() {
        viewModelScope.launch {
            _allAdsState.value = UiState.Loading
            fetchAds()
        }
    }

    fun toggleFavorite(adId: String) {
        viewModelScope.launch {
            repository.toggleFavorite(adId)
            fetchAds()
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    private suspend fun fetchAds() {
        repository.getAds()
            .onSuccess { ads -> _allAdsState.value = UiState.Success(ads) }
            .onFailure { error ->
                _allAdsState.value = UiState.Error(error.message ?: "Unknown error")
            }
    }
}