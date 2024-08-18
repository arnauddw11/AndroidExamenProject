package com.example.androidexamenproject.data

import com.example.androidexamenproject.model.EthereumAddress
import com.example.androidexamenproject.model.NFTContract
import kotlinx.coroutines.flow.Flow

/**
 * LocalRepository defines the interface for local data operations related to NFT contracts
 * and Ethereum addresses. This includes methods for inserting, updating, querying, and clearing data.
 */
interface LocalRepository {
    /**
     * Inserts an NFT contract into the local database.
     *
     * @param nftContracts The NFT contract to be inserted.
     */
    suspend fun insertContract(nftContracts: NFTContract)

    /**
     * Inserts an Ethereum address into the local database.
     *
     * @param ethereumAddress The Ethereum address to be inserted.
     */
    suspend fun insertEthereumAddress(ethereumAddress: EthereumAddress)

    /**
     * Updates the avatar URL for a specific Ethereum address in the local database.
     *
     * @param ethAddress The Ethereum address for which the avatar should be updated.
     * @param avatar The new avatar URL to be associated with the Ethereum address.
     */
    suspend fun insertAvatar(ethAddress: String, avatar: String)

    /**
     * Retrieves the avatar URL associated with the first Ethereum address in the local database.
     *
     * @return A Flow emitting the avatar URL, or null if no avatar exists.
     */
    suspend fun getAvatar(): Flow<String?>

    /**
     * Clears all Ethereum addresses from the local database.
     */
    suspend fun clearEthereumAddressTable()

    /**
     * Retrieves the first Ethereum address from the local database.
     *
     * @return A Flow emitting the Ethereum address, or null if no addresses exist.
     */
    suspend fun getEthereumAddress(): Flow<EthereumAddress?>

    /**
     * Clears all NFT contracts from the local database.
     */
    suspend fun clearContractsTable()

    /**
     * Retrieves all NFT contracts from the local database as a flow of a list.
     *
     * @return A Flow emitting a list of NFT contracts.
     */
    fun getContractsStream(): Flow<List<NFTContract>>
}

/**
 * OfflineLocalRepository is an implementation of LocalRepository that interacts with
 * the local Room database to perform CRUD operations on NFT contracts and Ethereum addresses.
 *
 * @property nftContractDao DAO for accessing NFT contract-related data.
 * @property ethereumAddressDao DAO for accessing Ethereum address-related data.
 */
class OfflineLocalRepository(
    private val nftContractDao: NFTContractDao,
    private val ethereumAddressDao: EthereumAddressDao
) : LocalRepository {

    /**
     * Inserts an NFT contract into the local database using the DAO.
     *
     * @param nftContract The NFT contract to be inserted.
     */
    override suspend fun insertContract(nftContract: NFTContract) =
        nftContractDao.insert(nftContract)

    /**
     * Retrieves a stream of all NFT contracts from the local database using the DAO.
     *
     * @return A Flow emitting a list of NFT contracts.
     */
    override fun getContractsStream(): Flow<List<NFTContract>> =
        nftContractDao.getNFTContracts()

    /**
     * Clears all NFT contracts from the local database using the DAO.
     */
    override suspend fun clearContractsTable() {
        nftContractDao.clearContractsTable()
    }

    /**
     * Inserts an Ethereum address into the local database using the DAO.
     *
     * @param ethereumAddress The Ethereum address to be inserted.
     */
    override suspend fun insertEthereumAddress(ethereumAddress: EthereumAddress) {
        ethereumAddressDao.insertAddress(ethereumAddress)
    }

    /**
     * Updates the avatar URL for a specific Ethereum address in the local database using the DAO.
     *
     * @param ethAddress The Ethereum address for which the avatar should be updated.
     * @param avatar The new avatar URL to be associated with the Ethereum address.
     */
    override suspend fun insertAvatar(ethAddress: String, avatar: String) {
        ethereumAddressDao.updateAvatar(ethAddress, avatar)
    }

    /**
     * Retrieves the avatar URL associated with the first Ethereum address from the local database using the DAO.
     *
     * @return A Flow emitting the avatar URL, or null if no avatar exists.
     */
    override suspend fun getAvatar(): Flow<String?> =
        ethereumAddressDao.getAvatar()

    /**
     * Clears all Ethereum addresses from the local database using the DAO.
     */
    override suspend fun clearEthereumAddressTable() {
        ethereumAddressDao.clearAddressTable()
    }

    /**
     * Retrieves the first Ethereum address from the local database using the DAO.
     *
     * @return A Flow emitting the Ethereum address, or null if no addresses exist.
     */
    override suspend fun getEthereumAddress(): Flow<EthereumAddress?> =
        ethereumAddressDao.getEthereumAddress()
}
