package com.paloma.idealista.di

import android.content.Context
import androidx.room.Room
import com.paloma.idealista.BuildConfig
import com.paloma.idealista.data.local.AppDatabase
import com.paloma.idealista.data.local.FavoriteAdDao
import com.paloma.idealista.data.remote.IdealistaApiService
import com.paloma.idealista.data.repository.AdsRepositoryImpl
import com.paloma.idealista.domain.repository.AdsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): IdealistaApiService {
        return retrofit.create(IdealistaApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            IDEALISTA_DATABASE
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

    private const val BASE_URL = "https://idealista.github.io/"
    private const val IDEALISTA_DATABASE = "idealista_database"
}