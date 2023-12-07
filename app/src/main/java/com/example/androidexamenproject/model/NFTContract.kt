package com.example.androidexamenproject.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.serialization.Serializable

@Serializable @Entity(tableName = "contracts")
data class NFTContract(
    @PrimaryKey
    val address: String,
    @ColumnInfo(name = "contract_name")
    val name: String?,
    val symbol: String?,
    val totalSupply: Int?,
    val tokenType: String?,
    val contractDeployer: String?,
    val deployedBlockNumber: Int?,
    @Embedded
    val openSeaMetadata: OpenSeaMetadata?,
    val totalBalance: String?,
    val numDistinctTokensOwned: String?,
    val isSpam: Boolean?,
    @Embedded
    val displayNft: DisplayNft?,
    @Embedded
    val image: Image?,
    val spamClassifications: List<String>?
)


@Serializable @Entity(tableName = "openseaMetadata")
data class OpenSeaMetadata (
    val floorPrice: Double?,
    val collectionName: String?,
    val safelistRequestStatus: String?,
    val imageUrl: String?,
    val description: String?,
    val externalUrl: String?,
    val twitterUsername: String?,
    val discordUrl: String?,
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
    val contentType: String?,
    val size: String?,
    val originalUrl: String?
)

class OpenSeaMetadataTypeConverter {
    @TypeConverter
    fun fromOpenSeaMetadata(openSeaMetadata: OpenSeaMetadata?): String? {
        return openSeaMetadata.toString()
        //return Gson().toJson(openSeaMetadata)
    }

    @TypeConverter
    fun toOpenSeaMetadata(openSeaMetadataString: String?): OpenSeaMetadata? {
        val type = object : TypeToken<OpenSeaMetadata>() {}.type
        return Gson().fromJson(openSeaMetadataString, type)
    }

}
class DisplayNftTypeConverter {
    @TypeConverter
    fun fromDisplayNft(displayNft: DisplayNft?): String? {
        return displayNft.toString()
        //return Gson().toJson(displayNft)
    }

    @TypeConverter
    fun toDisplayNft(displayNftString: String?): DisplayNft? {
        val type = object : TypeToken<DisplayNft>() {}.type
        return Gson().fromJson(displayNftString, type)
    }
}

class ImageTypeConverter {
    @TypeConverter
    fun fromImage(image: Image?): String? {
        return image.toString()
        //return Gson().toJson(image)
    }

    @TypeConverter
    fun toImage(imageString: String?): Image? {
        val type = object : TypeToken<Image>() {}.type
        return Gson().fromJson(imageString, type)
    }
}

class StringListTypeConverter {
    @TypeConverter
    fun fromStringList(stringList: List<String>?): String? {
       return stringList.toString()
        //return Gson().toJson(stringList)
    }

    @TypeConverter
    fun toStringList(stringListString: String?): List<String>? {
        val type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(stringListString, type)
    }
}