package com.paloma.idealista.data.mapper

import com.paloma.idealista.data.remote.dto.AdDto
import com.paloma.idealista.data.remote.dto.MultimediaDto
import com.paloma.idealista.data.remote.dto.PriceAmountDto
import com.paloma.idealista.data.remote.dto.PriceInfoDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class AdMapperTest {

    @Test
    fun `toDomain maps basic fields correctly`() {
        val dto = AdDto(
            propertyCode = "1",
            thumbnail = "https://example.com/img.jpg",
            floor = "2",
            price = 1195000.0,
            priceInfo = PriceInfoDto(price = PriceAmountDto(amount = 1195000.0, currencySuffix = "€")),
            propertyType = "flat",
            operation = "sale",
            size = 133.0,
            exterior = false,
            rooms = 3,
            bathrooms = 2,
            address = "calle de Lagasca",
            province = "Madrid",
            municipality = "Madrid",
            district = "Barrio de Salamanca",
            neighborhood = "Castellana",
            description = "Test description",
            multimedia = MultimediaDto(images = emptyList()),
            features = null,
            parkingSpace = null
        )

        val result = dto.toDomain()

        assertEquals("1", result.id)
        assertEquals(1195000.0, result.price, 0.0)
        assertEquals("€", result.currencySuffix)
        assertEquals("calle de Lagasca", result.address)
        assertEquals(3, result.rooms)
        assertFalse(result.isFavorite)
    }

    @Test
    fun `toDomain marks ad as favorite when favoritedAt is provided`() {
        val dto = AdDto(
            propertyCode = "1",
            thumbnail = null,
            floor = null,
            price = 1000.0,
            priceInfo = null,
            propertyType = null,
            operation = null,
            size = 50.0,
            exterior = null,
            rooms = 1,
            bathrooms = 1,
            address = null,
            province = null,
            municipality = null,
            district = null,
            neighborhood = null,
            description = null,
            multimedia = null,
            features = null,
            parkingSpace = null
        )

        val favoritedAt = 1234567890L
        val result = dto.toDomain(isFavorite = true, favoritedAt = favoritedAt)

        assertEquals(true, result.isFavorite)
        assertEquals(favoritedAt, result.favoritedAt)
    }

    @Test
    fun `toDomain falls back to flat price when priceInfo is null`() {
        val dto = AdDto(
            propertyCode = "2",
            thumbnail = null,
            floor = null,
            price = 500.0,
            priceInfo = null,
            propertyType = null,
            operation = null,
            size = 60.0,
            exterior = null,
            rooms = 2,
            bathrooms = 1,
            address = null,
            province = null,
            municipality = null,
            district = null,
            neighborhood = null,
            description = null,
            multimedia = null,
            features = null,
            parkingSpace = null
        )

        val result = dto.toDomain()

        assertEquals(500.0, result.price, 0.0)
        assertEquals("€", result.currencySuffix)
    }
}