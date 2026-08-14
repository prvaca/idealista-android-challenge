package com.paloma.idealista.data.remote

import com.paloma.idealista.data.remote.dto.AdDetailDto
import com.paloma.idealista.data.remote.dto.AdDto
import retrofit2.http.GET

interface IdealistaApiService {
    @GET("android-challenge/list.json")
    suspend fun getAds(): List<AdDto>

    @GET("android-challenge/detail.json")
    suspend fun getAdDetail(): AdDetailDto
}