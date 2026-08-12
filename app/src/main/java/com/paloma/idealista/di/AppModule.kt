package com.paloma.idealista.di

import android.content.Context
import androidx.room.Room
import com.paloma.idealista.data.local.AppDatabase
import com.paloma.idealista.data.local.FavoriteAdDao
import com.paloma.idealista.data.remote.IdealistaApiService
import com.paloma.idealista.data.remote.RetrofitClient
import com.paloma.idealista.data.repository.AdsRepositoryImpl
import com.paloma.idealista.domain.repository.AdsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "idealista_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideFavoriteAdDao(database: AppDatabase): FavoriteAdDao {
        return database.favoriteAdDao()
    }

    @Provides
    @Singleton
    fun provideAdsRepository(
        apiService: IdealistaApiService,
        favoriteAdDao: FavoriteAdDao
    ): AdsRepository {
        return AdsRepositoryImpl(apiService, favoriteAdDao)
    }
}