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

@Serializable
@Entity(tableName = "contracts")
/**
 * Represents an NFT contract entity with detailed information about the contract.
 *
 * @property address The unique address of the NFT contract on the blockchain.
 * @property name The name of the NFT contract.
 * @property totalSupply The total supply of tokens within the contract.
 * @property openSeaMetadata Metadata specific to OpenSea, such as floor price and collection details.
 * @property totalBalance The total balance of tokens owned by the user for this contract.
 * @property numDistinctTokensOwned The number of distinct tokens owned by the user within this contract.
 * @property displayNft The NFT that is selected to represent the contract.
 * @property image The image associated with the NFT contract.
 */
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
/**
 * Represents the metadata of an NFT, including its address, name, symbol, and optional image.
 *
 * @property address The unique address of the NFT on the blockchain.
 * @property name The name of the NFT.
 * @property symbol The symbol representing the NFT (e.g., "ETH").
 * @property openSeaMetadata Metadata specific to OpenSea, such as floor price and collection details.
 * @property image The image associated with the NFT.
 */
data class NftMetadata(
    val address: String,
    val name: String,
    val symbol: String,
    val openSeaMetadata: OpenSeaMetadata? = null,
    val image: Image? = null
)

@Serializable
@Entity(tableName = "openseaMetadata")
/**
 * Represents metadata provided by OpenSea for a specific NFT contract.
 *
 * @property floorPrice The current floor price of the NFT collection on OpenSea.
 * @property collectionName The name of the NFT collection.
 * @property imageUrl The URL to the collection's image.
 * @property description A description of the NFT collection.
 * @property externalUrl The external URL associated with the collection, often linking to a website.
 * @property lastIngestedAt The timestamp when this metadata was last updated or ingested.
 */
data class OpenSeaMetadata(
    val floorPrice: Double?,
    val collectionName: String?,
    val imageUrl: String?,
    val description: String?,
    val externalUrl: String?,
    @PrimaryKey
    val lastIngestedAt: String
)

@Serializable
@Entity(tableName = "displayNfts")
/**
 * Represents a specific NFT that is selected to be the display or featured NFT for a collection.
 *
 * @property tokenId The unique token ID of the NFT.
 * @property name The name of the NFT.
 */
data class DisplayNft(
    @PrimaryKey
    val tokenId: String,
    val name: String?
)

@Serializable
@Entity(tableName = "images")
/**
 * Represents various image URLs associated with an NFT, including cached, thumbnail, PNG, and original versions.
 *
 * @property cachedUrl The URL of the cached version of the image.
 * @property thumbnailUrl The URL of the thumbnail version of the image.
 * @property pngUrl The URL of the PNG version of the image.
 * @property originalUrl The URL of the original version of the image.
 */
data class Image(
    @PrimaryKey
    val cachedUrl: String,
    val thumbnailUrl: String?,
    val pngUrl: String?,
    val originalUrl: String?
)
