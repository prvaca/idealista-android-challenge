package com.paloma.idealista.data.repository

import com.paloma.idealista.data.local.FavoriteAdDao
import com.paloma.idealista.data.local.FavoriteAdEntity
import com.paloma.idealista.data.mapper.toDomain
import com.paloma.idealista.data.remote.IdealistaApiService
import com.paloma.idealista.domain.model.Ad
import com.paloma.idealista.domain.model.AdDetail
import com.paloma.idealista.domain.repository.AdsRepository

class AdsRepositoryImpl(
    private val apiService: IdealistaApiService,
    private val favoriteAdDao: FavoriteAdDao
) : AdsRepository {

    override suspend fun getAds(): Result<List<Ad>> {
        return runCatching {
            val favoritesMap = favoriteAdDao.getAllFavoritesOnceRaw().associate { it.adId to it.favoritedAt }

            apiService.getAds()
                .map { dto ->
                    val favoritedAt = favoritesMap[dto.propertyCode]
                    dto.toDomain(isFavorite = favoritedAt != null, favoritedAt = favoritedAt)
                }
                .distinctBy { it.id }
        }
    }

    override suspend fun getAdDetail(adId: String): Result<AdDetail> {
        return runCatching {
            val detailDto = apiService.getAdDetail()
            val favorite = favoriteAdDao.getFavorite(adId)
            detailDto.toDomain(isFavorite = favorite != null, favoritedAt = favorite?.favoritedAt)
        }
    }

    override suspend fun toggleFavorite(adId: String) {
        val existing = favoriteAdDao.getFavorite(adId)
        if (existing != null) {
            favoriteAdDao.deleteFavorite(existing)
        } else {
            favoriteAdDao.insertFavorite(
                FavoriteAdEntity(adId = adId, favoritedAt = System.currentTimeMillis())
            )
        }
    }
}