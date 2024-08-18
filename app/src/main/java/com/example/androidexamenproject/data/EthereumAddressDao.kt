package com.example.androidexamenproject.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.androidexamenproject.model.EthereumAddress
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for managing Ethereum addresses in the database.
 * Provides methods to insert, query, update, and clear Ethereum addresses.
 */
@Dao
interface EthereumAddressDao {

    /**
     * Inserts an Ethereum address into the database.
     * If an address with the same primary key already exists, it will be replaced.
     *
     * @param ethereumAddress The Ethereum address to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(ethereumAddress: EthereumAddress)

    /**
     * Retrieves the first Ethereum address from the database.
     * This method returns a Flow, allowing observation of changes to the data.
     *
     * @return A Flow emitting the first Ethereum address, or null if no addresses exist.
     */
    @Query("SELECT * FROM ethereumAddresses LIMIT 1")
    fun getEthereumAddress(): Flow<EthereumAddress?>

    /**
     * Deletes all Ethereum addresses from the database.
     * This will clear the entire ethereumAddresses table.
     */
    @Query("DELETE FROM ethereumAddresses")
    suspend fun clearAddressTable()

    /**
     * Updates the avatar URL for a specific Ethereum address in the database.
     *
     * @param ethAddress The Ethereum address for which the avatar should be updated.
     * @param avatar The new avatar URL to be associated with the Ethereum address.
     */
    @Query("UPDATE ethereumAddresses SET avatar = :avatar WHERE ethAddress = :ethAddress")
    suspend fun updateAvatar(ethAddress: String, avatar: String)

    /**
     * Retrieves the avatar URL from the first Ethereum address in the database.
     * This method returns a Flow, allowing observation of changes to the avatar.
     *
     * @return A Flow emitting the avatar URL, or null if no avatar exists.
     */
    @Query("SELECT avatar FROM ethereumAddresses LIMIT 1")
    fun getAvatar(): Flow<String?>
}
