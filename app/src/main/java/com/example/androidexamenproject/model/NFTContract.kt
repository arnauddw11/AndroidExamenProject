package com.example.androidexamenproject.model

import kotlinx.serialization.Serializable

@Serializable
data class NFTContract(
    val address: String?,
    val name: String?,
    val symbol: String?,
    val totalSupply: Int?,
    val tokenType: String?,
    val contractDeployer: String?,
    val deployedBlockNumber: Int?,
    val openSeaMetadata: OpenSeaMetadata?,
    val totalBalance: String?,
    val numDistinctTokensOwned: String?,
    val isSpam: Boolean?,
    val displayNft: DisplayNft?,
    val image: Image?,
    val spamClassifications: List<String>?
)

@Serializable
data class OpenSeaMetadata (
    val floorPrice: Double?,
    val collectionName: String?,
    val safelistRequestStatus: String?,
    val imageUrl: String?,
    val description: String?,
    val externalUrl: String?,
    val twitterUsername: String?,
    val discordUrl: String?
)
@Serializable
data class DisplayNft(
    val tokenId: String?,
    val name: String?
)
@Serializable
data class Image(
    val cachedUrl: String?,
    val thumbnailUrl: String?,
    val pngUrl: String?,
    val contentType: String?,
    val size: String?,
    val originalUrl: String?
)