package com.paloma.idealista.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paloma.idealista.domain.model.AdModel
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

    private val _allAdsState = MutableStateFlow<UiState<List<AdModel>>>(UiState.Loading)
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _showFavoritesOnly = MutableStateFlow(false)
    val showFavoritesOnly: StateFlow<Boolean> = _showFavoritesOnly.asStateFlow()

    private val _sortOrder = MutableStateFlow(SortOrder.NONE)
    val sortOrder: StateFlow<SortOrder> = _sortOrder.asStateFlow()

    private val _uiState = MutableStateFlow<UiState<List<AdModel>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<AdModel>>> = _uiState.asStateFlow()

    init {
        loadAds()

        combine(
            _allAdsState, _searchQuery, _showFavoritesOnly, _sortOrder
        ) { state, query, favoritesOnly, sortOrder ->
            when (state) {
                is UiState.Success -> {
                    var filtered = state.data
                    if (favoritesOnly) {
                        filtered = filtered.filter { it.isFavorite }
                    }
                    if (query.isNotBlank()) {
                        filtered = filtered.filter { ad ->
                            listOfNotNull(ad.address, ad.neighborhood, ad.district)
                                .any { it.contains(query, ignoreCase = true) }
                        }
                    }
                    filtered = when (sortOrder) {
                        SortOrder.PRICE_ASC -> filtered.sortedBy { it.price }
                        SortOrder.PRICE_DESC -> filtered.sortedByDescending { it.price }
                        SortOrder.NONE -> filtered
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

    fun toggleFavoritesFilter() {
        _showFavoritesOnly.value = !_showFavoritesOnly.value
    }

    fun toggleSortOrder() {
        _sortOrder.value = when (_sortOrder.value) {
            SortOrder.NONE -> SortOrder.PRICE_ASC
            SortOrder.PRICE_ASC -> SortOrder.PRICE_DESC
            SortOrder.PRICE_DESC -> SortOrder.NONE
        }
    }

    private suspend fun fetchAds() {
        repository.getAds()
            .onSuccess { ads -> _allAdsState.value = UiState.Success(ads) }
            .onFailure { error ->
                _allAdsState.value = UiState.Error(error.message)
            }
    }
}

enum class SortOrder {
    NONE, PRICE_ASC, PRICE_DESC
}
