package com.example.androidexamenproject.data

import com.example.androidexamenproject.network.ApiService
import kotlinx.serialization.json.JsonObject
import retrofit2.Response


interface AlchemyRepository {
    suspend fun getContractsForOwner(owner: String): Response<JsonObject>
    suspend fun getNFtsForOwner(owner: String, contractAddresses: List<String>): Response<JsonObject>
}

class NetworkAlchemyRepository(
    private val alchemyApiService: ApiService
) : AlchemyRepository {
    override suspend fun getContractsForOwner(owner: String): Response<JsonObject> =
        alchemyApiService.getContractsForOwner(owner)

    override suspend fun getNFtsForOwner(owner: String, contractAddresses: List<String>): Response<JsonObject> =
        alchemyApiService.getNFTsForOwner(owner, contractAddresses)



}




