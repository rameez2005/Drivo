package com.example.drivo.data.remote

import com.google.gson.annotations.SerializedName

data class FuelPriceDto(
    @SerializedName("totalArticles") val totalArticles: Int?,
    @SerializedName("articles") val articles: List<ArticleDto>?
)

data class ArticleDto(
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("content") val content: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("publishedAt") val publishedAt: String?,
    @SerializedName("source") val source: SourceDto?
)

data class SourceDto(
    @SerializedName("name") val name: String?,
    @SerializedName("url") val url: String?
)

data class FuelPrice(
    val title: String,
    val description: String,
    val content: String,
    val articleUrl: String,
    val imageUrl: String,
    val publishedAt: String,
    val sourceName: String
)

