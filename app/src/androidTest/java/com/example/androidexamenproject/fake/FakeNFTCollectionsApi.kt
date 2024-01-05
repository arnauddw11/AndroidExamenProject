package com.example.androidexamenproject.fake

import com.example.androidexamenproject.network.ApiService
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import retrofit2.Response

class FakeNFTCollectionsApi : ApiService {
    override suspend fun getContractsForOwner(
        owner: String,
        withMetadata: Boolean,
        excludeFilters: String,
        spamConfidenceLevel: String,
        pageSize: Int
    ): Response<JsonObject> {
        return Response.success(Json.encodeToJsonElement(FakeDataSource.fakeNfts).jsonObject)
    }

    override suspend fun getNFTsForOwner(
        owner: String,
        contractAddresses: List<String>,
        withMetadata: Boolean
    ): Response<JsonObject> {
        TODO("Not yet implemented")
    }
}