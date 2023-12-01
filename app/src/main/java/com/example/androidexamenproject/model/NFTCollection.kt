package com.example.androidexamenproject.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class NFTCollection(
    val name: String,
    val slug: String,
    val floorPrice: FloorPrice?,
    val description: String,
    val externalUrl: String?,
    val twitterUsername: String?,
    val discordUrl: String?,
    val contract: Contract?,
    val totalBalance: String,
    val numDistinctTokensOwned: String,
    val isSpam: Boolean,
    val displayNft: DisplayNft?,
    val image: Image?
)

@Serializable
data class FloorPrice(
    val marketplace: String?,
    val floorPrice: Double,
    @SerialName("priceCurrency")
    val priceCurrency: String
)

@Serializable
data class Contract(
    val address: String,
    val name: String,
    val symbol: String,
    val tokenType: String,
    val contractDeployer: String,
    val deployedBlockNumber: Long
)

@Serializable
data class DisplayNft(
    val tokenId: String,
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



