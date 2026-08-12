package com.paloma.idealista.data.remote.dto

data class AdDetailDto(
    val adId: Int,
    val price: Double,
    val priceInfo: DetailPriceInfoDto?,
    val operation: String?,
    val propertyType: String?,
    val extendedPropertyType: String?,
    val multimedia: MultimediaDto?,
    val propertyComment: String?,
    val ubication: UbicationDto?,
    val moreCharacteristics: MoreCharacteristicsDto?
)

data class DetailPriceInfoDto(
    val amount: Double,
    val currencySuffix: String?
)

data class UbicationDto(
    val latitude: Double,
    val longitude: Double
)

data class MoreCharacteristicsDto(
    val communityCosts: Double?,
    val roomNumber: Int?,
    val bathNumber: Int?,
    val exterior: Boolean?,
    val constructedArea: Double?,
    val lift: Boolean?,
    val boxroom: Boolean?,
    val floor: String?,
    val status: String?
)