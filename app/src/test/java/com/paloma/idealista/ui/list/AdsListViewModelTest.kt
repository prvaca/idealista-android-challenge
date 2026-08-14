package com.paloma.idealista.ui.list

import com.paloma.idealista.MainDispatcherRule
import com.paloma.idealista.domain.model.AdModel
import com.paloma.idealista.domain.repository.AdsRepository
import com.paloma.idealista.ui.common.UiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AdsListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val ad1 = AdModel(
        id = "1", thumbnailUrl = null, price = 1000.0, currencySuffix = "€",
        operation = "sale", size = 50.0, rooms = 2, bathrooms = 1,
        address = "calle de Lagasca", neighborhood = "Salamanca", district = "Centro",
        description = null, isFavorite = false, favoritedAt = null
    )

    private val ad2 = AdModel(
        id = "2", thumbnailUrl = null, price = 500.0, currencySuffix = "€",
        operation = "rent", size = 40.0, rooms = 1, bathrooms = 1,
        address = "calle de Fortuny", neighborhood = "Chamberí", district = "Norte",
        description = null, isFavorite = true, favoritedAt = 123456789L
    )

    private class FakeAdsRepository(
        private val ads: List<AdModel>
    ) : AdsRepository {
        override suspend fun getAds(): Result<List<AdModel>> = Result.success(ads)
        override suspend fun getAdDetail(adId: String) = error("Not used in this test")
        override suspend fun toggleFavorite(adId: String) {}
    }

    @Test
    fun `initial load returns all ads`() = runTest {
        val repository = FakeAdsRepository(listOf(ad1, ad2))
        val viewModel = AdsListViewModel(repository)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is UiState.Success)
        assertEquals(2, (state as UiState.Success).data.size)
    }

    @Test
    fun `search query filters by address`() = runTest {
        val repository = FakeAdsRepository(listOf(ad1, ad2))
        val viewModel = AdsListViewModel(repository)
        advanceUntilIdle()

        viewModel.onSearchQueryChanged("Fortuny")
        advanceUntilIdle()

        val state = viewModel.uiState.value as UiState.Success
        assertEquals(1, state.data.size)
        assertEquals("2", state.data.first().id)
    }

    @Test
    fun `favorites filter shows only favorited ads`() = runTest {
        val repository = FakeAdsRepository(listOf(ad1, ad2))
        val viewModel = AdsListViewModel(repository)
        advanceUntilIdle()

        viewModel.toggleFavoritesFilter()
        advanceUntilIdle()

        val state = viewModel.uiState.value as UiState.Success
        assertEquals(1, state.data.size)
        assertTrue(state.data.first().isFavorite)
    }

    @Test
    fun `sort order applies ascending price sort`() = runTest {
        val repository = FakeAdsRepository(listOf(ad1, ad2))
        val viewModel = AdsListViewModel(repository)
        advanceUntilIdle()

        viewModel.toggleSortOrder()
        advanceUntilIdle()

        val state = viewModel.uiState.value as UiState.Success
        assertEquals("2", state.data.first().id)
        assertEquals(500.0, state.data.first().price, 0.0)
    }
}