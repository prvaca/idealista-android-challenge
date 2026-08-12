package com.paloma.idealista.data.repository

import com.paloma.idealista.data.mapper.toDomain
import com.paloma.idealista.data.remote.IdealistaApiService
import com.paloma.idealista.domain.model.Ad
import com.paloma.idealista.domain.model.AdDetail
import com.paloma.idealista.domain.repository.AdsRepository

class AdsRepositoryImpl(
    private val apiService: IdealistaApiService
) : AdsRepository {

    override suspend fun getAds(): List<Ad> {
        return apiService.getAds().map { it.toDomain() }
    }

    override suspend fun getAdDetail(adId: String): AdDetail {
        return apiService.getAdDetail().toDomain()
    }

    override suspend fun toggleFavorite(adId: String) {
        // TODO: implement once Room persistence is added (feature/favorites)
    }
}