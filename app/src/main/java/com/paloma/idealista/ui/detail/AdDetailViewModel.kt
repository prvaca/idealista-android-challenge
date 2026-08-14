package com.paloma.idealista.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paloma.idealista.domain.model.AdDetailModel
import com.paloma.idealista.domain.repository.AdsRepository
import com.paloma.idealista.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdDetailViewModel @Inject constructor(
    private val repository: AdsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val adId: String = checkNotNull(savedStateHandle["adId"])

    private val _uiState = MutableStateFlow<UiState<AdDetailModel>>(UiState.Loading)
    val uiState: StateFlow<UiState<AdDetailModel>> = _uiState.asStateFlow()

    init {
        loadAdDetail()
    }

    fun loadAdDetail() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            fetchDetail()
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            repository.toggleFavorite(adId)
            fetchDetail()
        }
    }

    private suspend fun fetchDetail() {
        repository.getAdDetail(adId)
            .onSuccess { detail -> _uiState.value = UiState.Success(detail) }
            .onFailure { error ->
                _uiState.value = UiState.Error(error.message ?: "Unknown error")
            }
    }
}