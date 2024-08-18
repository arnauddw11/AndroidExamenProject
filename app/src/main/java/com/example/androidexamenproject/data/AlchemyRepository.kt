package com.example.androidexamenproject.data

import com.example.androidexamenproject.network.ApiService
import kotlinx.serialization.json.JsonObject
import retrofit2.Response



interface AlchemyRepository {
    suspend fun getContractsForOwner(owner: String): Response<JsonObject>
    suspend fun getNFtsForOwner(owner: String, contractAddresses: List<String>): Response<JsonObject>
    suspend fun computeRarity(contractAddress: String, tokenId: String): Response<JsonObject>
    suspend fun getNFTMetadata(contractAddress: String, tokenId: String): Response<JsonObject>
}

/**
 * NetworkAlchemyRepository is an implementation of the AlchemyRepository interface.
 * It interacts with the Alchemy API via the provided ApiService to fetch NFT-related data
 * and perform operations such as retrieving contracts, NFTs, and computing rarity.
 *
 * @param alchemyApiService The ApiService used to make network requests to the Alchemy API.
 */
class NetworkAlchemyRepository(
    private val alchemyApiService: ApiService
) : AlchemyRepository {

    /**
     * Fetches the list of NFT contracts owned by a specific address.
     *
     * @param owner The Ethereum address of the owner.
     * @return A Response containing a JsonObject with the contracts information.
     */
    override suspend fun getContractsForOwner(owner: String): Response<JsonObject> =
        alchemyApiService.getContractsForOwner(owner)

    /**
     * Fetches the NFTs owned by a specific address within the specified contracts.
     *
     * @param owner The Ethereum address of the owner.
     * @param contractAddresses The list of contract addresses to retrieve NFTs from.
     * @return A Response containing a JsonObject with the NFTs information.
     */
    override suspend fun getNFtsForOwner(owner: String, contractAddresses: List<String>): Response<JsonObject> =
        alchemyApiService.getNFTsForOwner(owner, contractAddresses)

    /**
     * Computes the rarity of a specific NFT based on its contract address and token ID.
     *
     * @param contractAddress The address of the NFT contract.
     * @param tokenId The unique token ID of the NFT.
     * @return A Response containing a JsonObject with the rarity information.
     */
    override suspend fun computeRarity(contractAddress: String, tokenId: String): Response<JsonObject> =
        alchemyApiService.computeRarity(contractAddress, tokenId)

    /**
     * Fetches the metadata for a specific NFT based on its contract address and token ID.
     *
     * @param contractAddress The address of the NFT contract.
     * @param tokenId The unique token ID of the NFT.
     * @return A Response containing a JsonObject with the metadata information.
     */
    override suspend fun getNFTMetadata(contractAddress: String, tokenId: String): Response<JsonObject> =
        alchemyApiService.getNFTMetadata(contractAddress, tokenId)
}





