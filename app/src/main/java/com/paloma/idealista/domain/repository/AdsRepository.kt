package com.paloma.idealista.domain.repository

import com.paloma.idealista.domain.model.Ad
import com.paloma.idealista.domain.model.AdDetail

interface AdsRepository {
    suspend fun getAds(): Result<List<Ad>>
    suspend fun getAdDetail(adId: String): Result<AdDetail>
    suspend fun toggleFavorite(adId: String)
    suspend fun getFavoriteAds(): Result<List<Ad>>

}