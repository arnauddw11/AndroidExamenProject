package com.example.androidexamenproject.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.androidexamenproject.model.EthereumAddress
import kotlinx.coroutines.flow.Flow

@Dao
interface EthereumAddressDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(ethereumAddress: EthereumAddress)

    @Query("SELECT * FROM ethereumAddresses LIMIT 1")
    fun getEthereumAddress(): Flow<EthereumAddress?>

    @Query("DELETE FROM ethereumAddresses")
    suspend fun clearAddressTable()

}