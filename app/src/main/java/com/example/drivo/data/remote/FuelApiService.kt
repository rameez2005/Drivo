package com.example.drivo.data.remote

import com.example.drivo.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface FuelApiService {
    @GET
    suspend fun getFuelPrices(@Url endpoint: String = BuildConfig.FUEL_API_ENDPOINT): Response<FuelPriceDto>
}

