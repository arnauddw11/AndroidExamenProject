package com.example.androidexamenproject.model

import androidx.room.Entity
import kotlinx.serialization.Serializable

@Serializable @Entity(tableName = "Contracts")
data class Contract(
    val address: String?,
    val name: String?,
    val symbol: String?,
    val totalSupply: String?,
    val tokenType: String?,
    val contractDeployer: String?,
    val deployedBlockNumber: String?,
    val openSeaMetadata: OpenSeaMetadata?,
    val isSpam: Boolean?,
    val spamClassifications: List<String>?,
)
@Serializable
data class OwnedNfts(
    val ownedNfts: List<NftObject>?,
    //val totalCount: String?,
    //val pageKey: String?,
    //val validAt: ValidAt?
)
@Serializable @Entity(tableName = "NftObjects")
data class NftObject(
    val contract: Contract?,
    val tokenId: String?,
    val tokenType: String?,
    val name: String?,
    val description: String?,
    val tokenUri: String?,
    val image: Image?,
    val raw: Raw?,
    val collection: Collection?,
    val mint: Mint?,
    val owners: List<String>?,
    val timeLastUpdated: String?,
    val balance: String?,
    val acquiredAt: AcquiredAt?
)
@Serializable @Entity(tableName = "Mint")
data class Mint(
    val mintAddress: String?,
    val blockNumber: String?,
    val timestamp: String?,
    val transactionHash: String?
)
@Serializable
data class ValidAt(
    val blockNumber: String?,
    val blockHash: String?,
    val blockTimestamp: String?
)

@Serializable @Entity(tableName = "Raw")
data class Raw(
    val tokenUri: String?,
    val metadata: MetaData?,
    val error: String?
)

@Serializable @Entity(tableName = "MetaData")
data class MetaData(
    val image: String?,
    val name: String?,
    val description: String?,
    val attributes: List<Attributes>?,
)

@Serializable @Entity(tableName = "Attributes")
data class Attributes(
    val value: String?,
    val traitType: String?
)

@Serializable @Entity(tableName = "Collections")
data class Collection(
    val name: String?,
    val slug: String?,
    val externalUrl: String?,
    val bannerImageUrl: String?
)

@Serializable @Entity(tableName = "AcquiredAt")
data class AcquiredAt(
    val blockTimestamp: String?,
    val blockNumber: String?,
)

