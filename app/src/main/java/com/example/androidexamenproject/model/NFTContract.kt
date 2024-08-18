package com.example.androidexamenproject.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
data class Rarity(
    val traitType: String,
    val value: String,
    val prevalence: Double
)

@Serializable @Entity(tableName = "contracts")
data class NFTContract(
    @PrimaryKey
    val address: String,
    @ColumnInfo(name = "contract_name")
    val name: String?,
    val totalSupply: Int?,
    @Embedded
    val openSeaMetadata: OpenSeaMetadata?,
    val totalBalance: String?,
    val numDistinctTokensOwned: String?,
    @Embedded
    val displayNft: DisplayNft?,
    @Embedded
    val image: Image?,
)

@Serializable
data class NftMetadata(
    val address: String,
    val name: String,
    val symbol: String,
    val openSeaMetadata: OpenSeaMetadata? = null,
    val image: Image? = null
)



@Serializable @Entity(tableName = "openseaMetadata")
data class OpenSeaMetadata (
    val floorPrice: Double?,
    val collectionName: String?,
    val imageUrl: String?,
    val description: String?,
    val externalUrl: String?,
    @PrimaryKey
    val lastIngestedAt: String
)
@Serializable @Entity(tableName = "displayNfts")
data class DisplayNft(
    @PrimaryKey
    val tokenId: String,
    val name: String?
)
@Serializable @Entity(tableName = "images")
data class Image(
    @PrimaryKey
    val cachedUrl: String,
    val thumbnailUrl: String?,
    val pngUrl: String?,
    val originalUrl: String?
)
