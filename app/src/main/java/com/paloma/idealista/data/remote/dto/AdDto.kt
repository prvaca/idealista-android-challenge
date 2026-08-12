package com.paloma.idealista.data.remote.dto

data class AdDto(
    val propertyCode: String,
    val thumbnail: String?,
    val floor: String?,
    val price: Double,
    val priceInfo: PriceInfoDto?,
    val propertyType: String?,
    val operation: String?,
    val size: Double,
    val exterior: Boolean?,
    val rooms: Int,
    val bathrooms: Int,
    val address: String?,
    val province: String?,
    val municipality: String?,
    val district: String?,
    val neighborhood: String?,
    val description: String?,
    val multimedia: MultimediaDto?,
    val features: FeaturesDto?,
    val parkingSpace: ParkingSpaceDto?
)

data class PriceInfoDto(
    val price: PriceAmountDto?
)

data class PriceAmountDto(
    val amount: Double,
    val currencySuffix: String?
)

data class MultimediaDto(
    val images: List<ImageDto>?
)

data class ImageDto(
    val url: String,
    val tag: String?
)

data class FeaturesDto(
    val hasAirConditioning: Boolean?,
    val hasBoxRoom: Boolean?,
    val hasSwimmingPool: Boolean?,
    val hasTerrace: Boolean?,
    val hasGarden: Boolean?
)

data class ParkingSpaceDto(
    val hasParkingSpace: Boolean?,
    val isParkingSpaceIncludedInPrice: Boolean?
)