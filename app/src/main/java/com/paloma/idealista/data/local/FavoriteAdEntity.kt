package com.paloma.idealista.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = FavoriteAdEntity.TABLE_NAME)
data class FavoriteAdEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_AD_ID)
    val adId: String,
    @ColumnInfo(name = COLUMN_FAVORITED_AT)
    val favoritedAt: Long
) {
    companion object {
        const val TABLE_NAME = "favorite_ads"
        const val COLUMN_AD_ID = "adId"
        const val COLUMN_FAVORITED_AT = "favoritedAt"
    }
}