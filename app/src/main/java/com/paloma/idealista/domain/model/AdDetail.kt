package com.paloma.idealista.domain.model

data class AdDetail(
    val id: String,
    val price: Double,
    val currencySuffix: String,
    val operation: String?,
    val propertyType: String?,
    val imageUrls: List<String>,
    val description: String?,
    val latitude: Double?,
    val longitude: Double?,
    val rooms: Int?,
    val bathrooms: Int?,
    val constructedArea: Double?,
    val floor: String?,
    val isFavorite: Boolean = false,
    val favoritedAt: Long? = null
)