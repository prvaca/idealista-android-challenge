package com.paloma.idealista.data.repository

import com.paloma.idealista.data.local.FavoriteAdDao
import com.paloma.idealista.data.local.FavoriteAdEntity
import com.paloma.idealista.data.remote.IdealistaApiService
import com.paloma.idealista.data.remote.dto.AdDto
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AdsRepositoryImplTest {

    private fun buildAdDto(propertyCode: String, price: Double = 1000.0) = AdDto(
        propertyCode = propertyCode,
        thumbnail = null,
        floor = null,
        price = price,
        priceInfo = null,
        propertyType = null,
        operation = "sale",
        size = 50.0,
        exterior = null,
        rooms = 2,
        bathrooms = 1,
        address = "test address",
        province = null,
        municipality = null,
        district = null,
        neighborhood = null,
        description = null,
        multimedia = null,
        features = null,
        parkingSpace = null
    )

    private class FakeApiService(
        private val ads: List<AdDto>
    ) : IdealistaApiService {
        override suspend fun getAds(): List<AdDto> = ads
        override suspend fun getAdDetail() = error("Not used in this test")
    }

    private class FakeFavoriteAdDao : FavoriteAdDao {
        private val favorites = mutableMapOf<String, FavoriteAdEntity>()

        override suspend fun getAllFavorites(): List<FavoriteAdEntity> = favorites.values.toList()
        override suspend fun getFavorite(adId: String): FavoriteAdEntity? = favorites[adId]
        override suspend fun insertFavorite(favorite: FavoriteAdEntity) {
            favorites[favorite.adId] = favorite
        }
        override suspend fun deleteFavorite(favorite: FavoriteAdEntity) {
            favorites.remove(favorite.adId)
        }
    }

    @Test
    fun `getAds removes duplicates by id`() = runTest {
        val duplicatedAds = listOf(
            buildAdDto("1"),
            buildAdDto("1"),
            buildAdDto("2")
        )
        val repository = AdsRepositoryImpl(FakeApiService(duplicatedAds), FakeFavoriteAdDao())

        val result = repository.getAds()

        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
    }

    @Test
    fun `getAds marks favorited ads correctly`() = runTest {
        val ads = listOf(buildAdDto("1"), buildAdDto("2"))
        val dao = FakeFavoriteAdDao()
        dao.insertFavorite(FavoriteAdEntity(adId = "1", favoritedAt = 123456789L))

        val repository = AdsRepositoryImpl(FakeApiService(ads), dao)
        val result = repository.getAds().getOrNull()!!

        val favoritedAd = result.first { it.id == "1" }
        val notFavoritedAd = result.first { it.id == "2" }

        assertTrue(favoritedAd.isFavorite)
        assertEquals(123456789L, favoritedAd.favoritedAt)
        assertTrue(!notFavoritedAd.isFavorite)
    }

    @Test
    fun `toggleFavorite adds favorite when not already favorited`() = runTest {
        val dao = FakeFavoriteAdDao()
        val repository = AdsRepositoryImpl(FakeApiService(emptyList()), dao)

        repository.toggleFavorite("1")

        assertTrue(dao.getFavorite("1") != null)
    }

    @Test
    fun `toggleFavorite removes favorite when already favorited`() = runTest {
        val dao = FakeFavoriteAdDao()
        dao.insertFavorite(FavoriteAdEntity(adId = "1", favoritedAt = 123456789L))
        val repository = AdsRepositoryImpl(FakeApiService(emptyList()), dao)

        repository.toggleFavorite("1")

        assertEquals(null, dao.getFavorite("1"))
    }
}