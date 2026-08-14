package com.paloma.idealista.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteAdDao {

    @Query("SELECT * FROM ${FavoriteAdEntity.TABLE_NAME}")
    suspend fun getAllFavorites(): List<FavoriteAdEntity>

    @Query("SELECT * FROM ${FavoriteAdEntity.TABLE_NAME} WHERE ${FavoriteAdEntity.COLUMN_AD_ID} = :adId LIMIT 1")
    suspend fun getFavorite(adId: String): FavoriteAdEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteAdEntity)

    @Delete
    suspend fun deleteFavorite(favorite: FavoriteAdEntity)
}