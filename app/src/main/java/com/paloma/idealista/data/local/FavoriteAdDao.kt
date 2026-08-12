package com.paloma.idealista.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteAdDao {

    @Query("SELECT * FROM favorite_ads")
    fun observeFavorites(): Flow<List<FavoriteAdEntity>>

    @Query("SELECT * FROM favorite_ads WHERE adId = :adId LIMIT 1")
    suspend fun getFavorite(adId: String): FavoriteAdEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteAdEntity)

    @Delete
    suspend fun deleteFavorite(favorite: FavoriteAdEntity)

    @Query("SELECT * FROM favorite_ads")
    suspend fun getAllFavoritesOnceRaw(): List<FavoriteAdEntity>
}