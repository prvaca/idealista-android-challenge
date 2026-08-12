package com.paloma.idealista.domain.repository

import com.paloma.idealista.domain.model.Ad
import com.paloma.idealista.domain.model.AdDetail

interface AdsRepository {
    suspend fun getAds(): List<Ad>
    suspend fun getAdDetail(adId: String): AdDetail
    suspend fun toggleFavorite(adId: String)
}