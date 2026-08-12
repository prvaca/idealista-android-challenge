package com.paloma.idealista.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_ads")
data class FavoriteAdEntity(
    @PrimaryKey val adId: String,
    val favoritedAt: Long
)