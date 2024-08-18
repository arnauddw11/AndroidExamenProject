package com.example.androidexamenproject.model

import androidx.room.Embedded
import androidx.room.Entity
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "contractsOwnedNfts")
/**
 * Represents a contract associated with owned NFTs.
 *
 * @property address The contract address.
 * @property name The name of the contract.
 * @property symbol The symbol of the contract.
 * @property totalSupply The total supply of tokens in the contract.
 * @property tokenType The type of tokens in the contract.
 * @property contractDeployer The address that deployed the contract.
 * @property deployedBlockNumber The block number at which the contract was deployed.
 * @property openSeaMetadata Metadata associated with OpenSea for this contract.
 * @property isSpam Indicates if the contract is classified as spam.
 * @property spamClassifications List of classifications indicating why the contract is considered spam.
 */
data class Contract(
    val address: String?,
    val name: String?,
    val symbol: String?,
    val totalSupply: String?,
    val tokenType: String?,
    val contractDeployer: String?,
    val deployedBlockNumber: String?,
    @Embedded
    val openSeaMetadata: OpenSeaMetadata?,
    val isSpam: Boolean?,
    val spamClassifications: List<String>?,
)

@Serializable
@Entity(tableName = "NftObjects")
/**
 * Represents an NFT (Non-Fungible Token) object.
 *
 * @property contract The contract associated with the NFT.
 * @property tokenId The unique token ID of the NFT.
 * @property tokenType The type of token (e.g., ERC-721, ERC-1155).
 * @property name The name of the NFT.
 * @property description A description of the NFT.
 * @property tokenUri The URI where the token's metadata can be retrieved.
 * @property image The image associated with the NFT.
 * @property raw The raw metadata and token URI information.
 * @property collection The collection to which the NFT belongs.
 * @property mint Information about the minting of the NFT.
 * @property owners List of addresses that own this NFT.
 * @property timeLastUpdated The last time the NFT metadata was updated.
 * @property balance The balance of this token if it's a fungible token or ERC-1155.
 * @property acquiredAt Information about when the NFT was acquired.
 */
data class NftObject(
    val contract: Contract?,
    val tokenId: String?,
    val tokenType: String?,
    val name: String?,
    val description: String?,
    val tokenUri: String?,
    val image: Image?,
    @Embedded
    val raw: Raw?,
    @Embedded
    val collection: Collection?,
    @Embedded
    val mint: Mint?,
    val owners: List<String>?,
    val timeLastUpdated: String?,
    val balance: String?,
    @Embedded
    val acquiredAt: AcquiredAt?
)

@Serializable
@Entity(tableName = "Mint")
/**
 * Represents the minting information of an NFT.
 *
 * @property mintAddress The address where the NFT was minted.
 * @property blockNumber The block number during which the NFT was minted.
 * @property timestamp The timestamp when the NFT was minted.
 * @property transactionHash The hash of the transaction that minted the NFT.
 */
data class Mint(
    val mintAddress: String?,
    val blockNumber: String?,
    val timestamp: String?,
    val transactionHash: String?
)

@Serializable
@Entity(tableName = "Raw")
/**
 * Represents the raw data related to the NFT, including its metadata and token URI.
 *
 * @property tokenUri The URI for the token's metadata.
 * @property metadata The detailed metadata of the NFT.
 * @property error Any errors encountered when fetching the metadata.
 */
data class Raw(
    val tokenUri: String?,
    @Embedded
    val metadata: MetaData?,
    val error: String?
)

@Serializable
@Entity(tableName = "MetaData")
/**
 * Represents the metadata of an NFT.
 *
 * @property image The image URL of the NFT.
 * @property name The name of the NFT.
 * @property description A description of the NFT.
 * @property attributes A list of attributes associated with the NFT.
 */
data class MetaData(
    val image: String?,
    val name: String?,
    val description: String?,
    val attributes: List<Attributes>?,
)

@Serializable
@Entity(tableName = "Attributes")
/**
 * Represents an attribute of an NFT, typically used for traits in collectibles.
 *
 * @property value The value of the attribute.
 * @property traitType The type or category of the attribute (e.g., "color", "rarity").
 */
data class Attributes(
    val value: String?,
    val traitType: String?
)

@Serializable
@Entity(tableName = "Collections")
/**
 * Represents a collection of NFTs.
 *
 * @property name The name of the collection.
 * @property slug A unique identifier or slug for the collection, often used in URLs.
 * @property externalUrl The external URL associated with the collection.
 * @property bannerImageUrl The banner image URL for the collection.
 */
data class Collection(
    val name: String?,
    val slug: String?,
    val externalUrl: String?,
    val bannerImageUrl: String?
)

@Serializable
@Entity(tableName = "AcquiredAt")
/**
 * Represents the details of when and where an NFT was acquired.
 *
 * @property blockTimestamp The timestamp of the block when the NFT was acquired.
 * @property blockNumber The block number when the NFT was acquired.
 */
data class AcquiredAt(
    val blockTimestamp: String?,
    val blockNumber: String?,
)
