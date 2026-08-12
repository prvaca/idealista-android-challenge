package com.paloma.idealista.data.mapper

import com.paloma.idealista.data.remote.dto.AdDetailDto
import com.paloma.idealista.data.remote.dto.AdDto
import com.paloma.idealista.domain.model.Ad
import com.paloma.idealista.domain.model.AdDetail

fun AdDto.toDomain(isFavorite: Boolean = false, favoritedAt: Long? = null): Ad {
    return Ad(
        id = propertyCode,
        thumbnailUrl = thumbnail,
        price = priceInfo?.price?.amount ?: price,
        currencySuffix = priceInfo?.price?.currencySuffix ?: "€",
        operation = operation,
        size = size,
        rooms = rooms,
        bathrooms = bathrooms,
        address = address,
        neighborhood = neighborhood,
        district = district,
        description = description,
        isFavorite = isFavorite,
        favoritedAt = favoritedAt
    )
}

fun AdDetailDto.toDomain(isFavorite: Boolean = false, favoritedAt: Long? = null): AdDetail {
    return AdDetail(
        id = adId.toString(),
        price = priceInfo?.amount ?: price,
        currencySuffix = priceInfo?.currencySuffix ?: "€",
        operation = operation,
        propertyType = propertyType,
        imageUrls = multimedia?.images?.map { it.url } ?: emptyList(),
        description = propertyComment,
        latitude = ubication?.latitude,
        longitude = ubication?.longitude,
        rooms = moreCharacteristics?.roomNumber,
        bathrooms = moreCharacteristics?.bathNumber,
        constructedArea = moreCharacteristics?.constructedArea,
        floor = moreCharacteristics?.floor,
        isFavorite = isFavorite,
        favoritedAt = favoritedAt
    )
}