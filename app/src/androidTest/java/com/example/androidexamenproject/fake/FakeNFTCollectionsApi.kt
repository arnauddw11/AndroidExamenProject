package com.example.androidexamenproject.fake

import com.example.androidexamenproject.network.ApiService
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import retrofit2.Response

class FakeNFTCollectionsApi : ApiService {
    public suspend fun getContractsForOwner(
        owner: String,
        withMetadata: Boolean,
        excludeFilters: String,
        spamConfidenceLevel: String,
        pageSize: Int
    ): Response<JsonObject> {
        return Response.success(Json.encodeToJsonElement(FakeDataSource.fakeNfts).jsonObject)
    }

    override suspend fun getContractsForOwner(
        owner: String,
        withMetadata: Boolean,
        excludeFilters: String,
        pageSize: Int,
    ): Response<JsonObject> {
        TODO("Not yet implemented")
    }

    override suspend fun getNFTsForOwner(
        owner: String,
        contractAddresses: List<String>,
        withMetadata: Boolean
    ): Response<JsonObject> {
        TODO("Not yet implemented")
    }

    override suspend fun computeRarity(
        contractAddress: String,
        tokenId: String,
    ): Response<JsonObject> {
        TODO("Not yet implemented")
    }

    override suspend fun getNFTMetadata(
        contractAddress: String,
        tokenId: String,
    ): Response<JsonObject> {
        TODO("Not yet implemented")
    }
}