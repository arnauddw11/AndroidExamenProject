package com.example.androidexamenproject.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.androidexamenproject.model.DisplayNft
import com.example.androidexamenproject.model.EthereumAddress
import com.example.androidexamenproject.model.Image
import com.example.androidexamenproject.model.NFTContract
import com.example.androidexamenproject.model.OpenSeaMetadata

/**
 * The Room database for the NFT application.
 * It includes the database schema and versioning, as well as DAOs for accessing data.
 *
 * @property nftContractDao Provides access to operations related to NFT contracts.
 * @property ethereumAddressDao Provides access to operations related to Ethereum addresses.
 */
@Database(entities = [NFTContract::class, OpenSeaMetadata::class, DisplayNft::class, Image::class, EthereumAddress::class], version = 3, exportSchema = false)
abstract class NFTAppDatabase : RoomDatabase() {

    /**
     * Provides access to the DAO for NFT contracts.
     *
     * @return The NFTContractDao for managing NFT contracts.
     */
    abstract fun nftContractDao(): NFTContractDao

    /**
     * Provides access to the DAO for Ethereum addresses.
     *
     * @return The EthereumAddressDao for managing Ethereum addresses.
     */
    abstract fun ethereumAddressDao(): EthereumAddressDao

    companion object {
        @Volatile
        private var INSTANCE: NFTAppDatabase? = null

        /**
         * Gets the singleton instance of the NFTAppDatabase.
         * If the instance is null, it initializes the database.
         *
         * @param context The application context used to initialize the database.
         * @return The singleton instance of NFTAppDatabase.
         */
        fun getDatabase(context: Context): NFTAppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    NFTAppDatabase::class.java,
                    "nft_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .addMigrations(MIGRATION_2_3)
                    .build()
                    .also { INSTANCE = it }
            }
        }

        /**
         * Migration from database version 1 to version 2.
         * Adds the 'ensAddress' column to the 'ethereumAddresses' table.
         */
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE ethereumAddresses ADD COLUMN ensAddress TEXT")
            }
        }

        /**
         * Migration from database version 2 to version 3.
         * Adds the 'avatar' column to the 'ethereumAddresses' table.
         */
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE ethereumAddresses ADD COLUMN avatar TEXT")
            }
        }
    }
}
