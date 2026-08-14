package com.paloma.idealista.domain.model

data class AdModel(
    val id: String,
    val thumbnailUrl: String?,
    val price: Double,
    val currencySuffix: String,
    val operation: String?,
    val size: Double,
    val rooms: Int,
    val bathrooms: Int,
    val address: String?,
    val neighborhood: String?,
    val district: String?,
    val description: String?,
    val isFavorite: Boolean = false,
    val favoritedAt: Long? = null
)