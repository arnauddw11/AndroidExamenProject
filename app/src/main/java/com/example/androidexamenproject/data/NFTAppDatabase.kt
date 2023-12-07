package com.example.androidexamenproject.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.androidexamenproject.model.DisplayNft
import com.example.androidexamenproject.model.DisplayNftTypeConverter
import com.example.androidexamenproject.model.EthereumAddress
import com.example.androidexamenproject.model.Image
import com.example.androidexamenproject.model.ImageTypeConverter
import com.example.androidexamenproject.model.NFTContract
import com.example.androidexamenproject.model.OpenSeaMetadata
import com.example.androidexamenproject.model.OpenSeaMetadataTypeConverter
import com.example.androidexamenproject.model.StringListTypeConverter

@TypeConverters(value = [ImageTypeConverter::class, OpenSeaMetadataTypeConverter::class, DisplayNftTypeConverter::class, StringListTypeConverter::class])
@Database(entities = [NFTContract::class, OpenSeaMetadata::class, DisplayNft::class, Image::class, EthereumAddress::class], version = 1, exportSchema = false)
abstract class NFTAppDatabase : RoomDatabase() {
    abstract fun nftContractDao(): NFTContractDao
    abstract fun ethereumAddressDao(): EthereumAddressDao

    companion object {
        @Volatile
        private var INSTANCE: NFTAppDatabase? = null

        fun getDatabase(context: Context): NFTAppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    NFTAppDatabase::class.java,
                    "nft_database"
                )
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}

