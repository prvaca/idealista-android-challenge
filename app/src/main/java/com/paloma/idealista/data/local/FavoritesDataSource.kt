package com.paloma.idealista.data.local

import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesDataSource @Inject constructor() {

    private val _favorites = MutableStateFlow<Map<String, Long>>(emptyMap())
    val favorites get() = _favorites.value

    fun toggleFavorite(adId: String) {
        val current = _favorites.value.toMutableMap()
        if (current.containsKey(adId)) {
            current.remove(adId)
        } else {
            current[adId] = System.currentTimeMillis()
        }
        _favorites.value = current
    }
}