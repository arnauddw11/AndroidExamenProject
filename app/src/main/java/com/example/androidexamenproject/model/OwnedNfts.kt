package com.example.androidexamenproject.model

import androidx.room.Embedded
import androidx.room.Entity
import kotlinx.serialization.Serializable

@Serializable @Entity(tableName = "contractsOwnedNfts")
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
@Serializable @Entity(tableName = "NftObjects")
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
@Serializable @Entity(tableName = "Mint")
data class Mint(
    val mintAddress: String?,
    val blockNumber: String?,
    val timestamp: String?,
    val transactionHash: String?
)

@Serializable @Entity(tableName = "Raw")
data class Raw(
    val tokenUri: String?,
    @Embedded
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

