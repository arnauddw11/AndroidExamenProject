package com.example.androidexamenproject.model

import java.time.LocalDateTime

data class NFTCollectionMetaData (
    val address: String,
    val name: String?,
    val symbol: String?,
    val totalSupply: String?,
    val tokenType: String?,
    val contractDeployer: String?,
    val deployedBlockNumber: String?,
    val openSeaMetadata: OpenseaMetadata?

)

data class OpenseaMetadata (
    val floorprice: Double?,
    val collectionName: String?,
    val safelistRequestStatus: String?,
    val imageUrl: String?,
    val description: String?,
    val externalUrl: String?,
    val twitterUsername: String?,
    val discordUrl: String?
)