package com.paloma.idealista.domain.repository

import com.paloma.idealista.domain.model.AdModel
import com.paloma.idealista.domain.model.AdDetailModel

interface AdsRepository {
    suspend fun getAds(): Result<List<AdModel>>
    suspend fun getAdDetail(adId: String): Result<AdDetailModel>
    suspend fun toggleFavorite(adId: String)
}