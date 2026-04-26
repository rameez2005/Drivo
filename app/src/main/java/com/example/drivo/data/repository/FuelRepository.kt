package com.example.drivo.data.repository

import com.example.drivo.data.remote.FuelPrice
import com.example.drivo.data.remote.RetrofitProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FuelRepository {

    suspend fun fetchFuelPrices(): Result<List<FuelPrice>> = withContext(Dispatchers.IO) {
        runCatching {
            val response = RetrofitProvider.fuelApiService.getFuelPrices()
            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string()?.take(200).orEmpty()
                error("API request failed (HTTP ${response.code()}) ${errorBody}".trim())
            }

            val body = response.body()
            body?.articles.orEmpty().map {
                FuelPrice(
                    title = it.title ?: "Untitled",
                    description = it.description ?: "",
                    content = it.content ?: "",
                    articleUrl = it.url ?: "",
                    imageUrl = it.image ?: "",
                    publishedAt = it.publishedAt ?: "N/A",
                    sourceName = it.source?.name ?: "Unknown source"
                )
            }
        }
    }
}

