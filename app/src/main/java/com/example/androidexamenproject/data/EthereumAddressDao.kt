package com.example.androidexamenproject.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.androidexamenproject.model.EthereumAddress
import kotlinx.coroutines.flow.Flow

@Dao
interface EthereumAddressDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ethereumAddress: EthereumAddress)
    @Update
    suspend fun update(ethereumAddress: EthereumAddress)
    @Delete
    suspend fun delete(ethereumAddress: EthereumAddress)

    @Query("SELECT * FROM ethereumAddresses")
    fun getEthereumAddress(): Flow<List<EthereumAddress>>
}