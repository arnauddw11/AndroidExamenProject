package com.example.androidexamenproject.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.androidexamenproject.model.NFTContract
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for managing NFT contracts in the database.
 * Provides methods to insert, update, delete, and query NFT contracts.
 */
@Dao
interface NFTContractDao {

    /**
     * Inserts an NFT contract into the database.
     * If a contract with the same primary key already exists, it will be replaced.
     *
     * @param nftContract The NFT contract to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(nftContract: NFTContract)

    /**
     * Updates an existing NFT contract in the database.
     *
     * @param nftContract The NFT contract with updated values.
     */
    @Update
    suspend fun update(nftContract: NFTContract)

    /**
     * Deletes an NFT contract from the database.
     *
     * @param nftContract The NFT contract to be deleted.
     */
    @Delete
    suspend fun delete(nftContract: NFTContract)

    /**
     * Retrieves all NFT contracts from the database as a flow of a list.
     * This allows observing changes to the data in a reactive manner.
     *
     * @return A Flow emitting a list of NFT contracts.
     */
    @Query("SELECT * FROM contracts")
    fun getNFTContracts(): Flow<List<NFTContract>>

    /**
     * Deletes all NFT contracts from the database.
     * This will clear the entire contracts table.
     */
    @Query("DELETE FROM contracts")
    suspend fun clearContractsTable()
}
