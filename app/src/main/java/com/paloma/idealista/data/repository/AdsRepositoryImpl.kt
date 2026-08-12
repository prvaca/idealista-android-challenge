package com.paloma.idealista.data.repository

import com.paloma.idealista.data.local.FavoritesDataSource
import com.paloma.idealista.data.mapper.toDomain
import com.paloma.idealista.data.remote.IdealistaApiService
import com.paloma.idealista.domain.model.Ad
import com.paloma.idealista.domain.model.AdDetail
import com.paloma.idealista.domain.repository.AdsRepository

class AdsRepositoryImpl(
    private val apiService: IdealistaApiService,
    private val favoritesDataSource: FavoritesDataSource
) : AdsRepository {

    override suspend fun getAds(): Result<List<Ad>> {
        return runCatching {
            val favorites = favoritesDataSource.favorites
            apiService.getAds()
                .map { dto ->
                    val favoritedAt = favorites[dto.propertyCode]
                    dto.toDomain(isFavorite = favoritedAt != null, favoritedAt = favoritedAt)
                }
                .distinctBy { it.id }
        }
    }

    override suspend fun getAdDetail(adId: String): Result<AdDetail> {
        return runCatching {
            val detailDto = apiService.getAdDetail()
            val realId = detailDto.adId.toString()
            val favoritedAt = favoritesDataSource.favorites[realId]
            detailDto.toDomain(isFavorite = favoritedAt != null, favoritedAt = favoritedAt)
        }
    }

    override suspend fun toggleFavorite(adId: String) {
        favoritesDataSource.toggleFavorite(adId)
    }
}