package com.example.androidexamenproject.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.androidexamenproject.model.DisplayNft
import com.example.androidexamenproject.model.EthereumAddress
import com.example.androidexamenproject.model.Image
import com.example.androidexamenproject.model.NFTContract
import com.example.androidexamenproject.model.OpenSeaMetadata

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

