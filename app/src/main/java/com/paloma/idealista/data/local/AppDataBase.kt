package com.paloma.idealista.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteAdEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteAdDao(): FavoriteAdDao
}