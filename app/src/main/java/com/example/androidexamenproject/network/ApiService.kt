package com.example.androidexamenproject.network

import kotlinx.serialization.json.JsonObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("getContractsForOwner")
    suspend fun getContractsForOwner(
        @Query("owner") owner: String,
        @Query("withMetadata") withMetadata: Boolean = true,
        @Query("excludeFilters[]") excludeFilters: String = "SPAM",
        @Query("spamConfidenceLevel") spamConfidenceLevel: String = "HIGH",
        @Query("pageSize") pageSize: Int = 100,
    ): Response<JsonObject>

    @GET("getNFTsForOwner")
    suspend fun getNFTsForOwner(
        @Query("owner") owner: String,
        @Query("contractAddresses[]") contractAddresses: List<String>,
        @Query("withMetadata") withMetadata: Boolean = true,
        //@Query("excludeFilters[]") excludeFilters: String = "SPAM",
        //@Query("spamConfidenceLevel") spamConfidenceLevel: String = "HIGH",
        //@Query("pageSize") pageSize: Int = 100,
    ): Response<JsonObject>
}
