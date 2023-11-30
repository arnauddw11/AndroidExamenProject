package com.example.androidexamenproject.data

import com.example.androidexamenproject.network.ApiService
import kotlinx.serialization.json.JsonObject
import retrofit2.Response


interface AlchemyRepository {
    suspend fun getCollectionsForOwner(owner: String): Response<JsonObject>
    suspend fun getNFTsForOwner(owner: String, contractAddress: String): Response<JsonObject>
    suspend fun getContractMetadata(contractAddress: String): Response<JsonObject>
}

class NetworkAlchemyRepository(private val alchemyApiService: ApiService) : AlchemyRepository {
    override suspend fun getCollectionsForOwner(owner: String): Response<JsonObject> = alchemyApiService.getCollectionsForOwner(owner)
    override suspend fun getNFTsForOwner(owner: String, contractAddress: String): Response<JsonObject> = alchemyApiService.getNFTsForOwner(owner, contractAddress)
    override suspend fun getContractMetadata(contractAddress: String): Response<JsonObject> = alchemyApiService.getContractMetadata(contractAddress)
}
