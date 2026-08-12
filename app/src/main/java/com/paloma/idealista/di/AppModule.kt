package com.paloma.idealista.di

import com.paloma.idealista.data.local.FavoritesDataSource
import com.paloma.idealista.data.remote.IdealistaApiService
import com.paloma.idealista.data.remote.RetrofitClient
import com.paloma.idealista.data.repository.AdsRepositoryImpl
import com.paloma.idealista.domain.repository.AdsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiService(): IdealistaApiService {
        return RetrofitClient.apiService
    }

    @Provides
    @Singleton
    fun provideAdsRepository(
        apiService: IdealistaApiService,
        favoritesDataSource: FavoritesDataSource
    ): AdsRepository {
        return AdsRepositoryImpl(apiService, favoritesDataSource)
    }
}