package com.example.drivo.data.remote

import com.example.drivo.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitProvider {

    val fuelApiService: FuelApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.FUEL_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FuelApiService::class.java)
    }
}

