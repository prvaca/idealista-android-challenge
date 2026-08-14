package com.paloma.idealista.data.remote

import com.paloma.idealista.data.remote.dto.AdDetailDto
import com.paloma.idealista.data.remote.dto.AdDto
import retrofit2.http.GET

interface IdealistaApiService {

    @GET("android-challenge/list.json")
    suspend fun getAds(): List<AdDto>

    /**
     * Known API limitation: this endpoint always returns the same fixed ad
     * (adid = 1), regardless of which ad was selected from the list.
     * This is documented behavior of the challenge's mock API, not a bug
     * in this implementation.
     */
    @GET("android-challenge/detail.json")
    suspend fun getAdDetail(): AdDetailDto
}