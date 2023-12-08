package com.example.androidexamenproject.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.androidexamenproject.model.NFTContract
import kotlinx.coroutines.flow.Flow

@Dao
interface NFTContractDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(nftContract: NFTContract)
    @Update
    suspend fun update(nftContract: NFTContract)
    @Delete
    suspend fun delete(nftContract: NFTContract)
    @Query("SELECT * FROM contracts")
    fun getNFTContracts(): Flow<List<NFTContract>>
    @Query("DELETE FROM contracts")
    suspend fun clearContractsTable()

}