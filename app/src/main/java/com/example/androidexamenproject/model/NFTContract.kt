package com.example.androidexamenproject.model

import kotlinx.serialization.Serializable
/*
@Serializable
data class NFTContract(
    val address: String,
    val name: String,
    val symbol: String,
    val totalSupply: Int?, // Change the type accordingly if totalSupply is not always an integer
    val tokenType: String,
    val contractDeployer: String,
    val deployedBlockNumber: Int,
    val openSeaMetadata: OpenSeaMetadata,
    val isSpam: Boolean,
    val spamClassifications: List<String>
)

@Serializable
data class OpenSeaMetadata(
    val floorPrice: Double?, // Change the type accordingly if floorPrice is not always a double
    val collectionName: String,
    val collectionSlug: String,
    val safelistRequestStatus: String,
    val imageUrl: String,
    val description: String,
    val externalUrl: String,
    val twitterUsername: String?,
    val discordUrl: String?,
    val bannerImageUrl: String,
    val lastIngestedAt: String
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
@Serializable
data class Raw(
    val tokenUri: String,
    val metadata: Map<String, String>,
    val error: String?
)

@Serializable
data class Collection(
    val name: String,
    val slug: String,
    val externalUrl: String,
    val bannerImageUrl: String
)

@Serializable
data class Mint(
    val mintAddress: String?,
    val blockNumber: Int?,
    val timestamp: String?,
    val transactionHash: String?
)
@Serializable
data class AcquiredAt(
    val blockTimestamp: String?,
    val blockNumber: Int?
)

@Serializable
data class OwnedNft(
    val contract: Contract,
    val tokenId: String,
    val tokenType: String,
    val name: String?,
    val description: String?,
    val tokenUri: String,
    val image: Image,
    val raw: Raw,
    val collection: Collection,
    val mint: Mint,
    val owners: List<Any>?, // Change the type accordingly if owners have a different structure
    val timeLastUpdated: String,
    val balance: String,
    val acquiredAt: AcquiredAt
)

@Serializable
data class YourResponse(
    val ownedNfts: List<OwnedNft>
)

 */
