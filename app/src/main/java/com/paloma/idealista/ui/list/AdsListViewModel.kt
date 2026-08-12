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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdsListViewModel @Inject constructor(
    private val repository: AdsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Ad>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Ad>>> = _uiState.asStateFlow()

    init {
        loadAds()
    }

    fun loadAds() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getAds()
                .onSuccess { ads -> _uiState.value = UiState.Success(ads) }
                .onFailure { error ->
                    _uiState.value = UiState.Error(error.message ?: "Unknown error")
                }
        }
    }

    fun toggleFavorite(adId: String) {
        viewModelScope.launch {
            repository.toggleFavorite(adId)
            loadAds()
        }
    }
}