package com.example.androidexamenproject.data

import com.example.androidexamenproject.model.EthereumAddress
import com.example.androidexamenproject.model.NFTContract
import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    suspend fun insertContract(nftContracts: NFTContract)
    suspend fun insertEthereumAddress(ethereumAddress: EthereumAddress)
    suspend fun clearEthereumAddressTable()
    suspend fun getEthereumAddress(): Flow<EthereumAddress?>
    suspend fun clearContractsTable()
    fun getContractsStream(): Flow<List<NFTContract>>
}

class OfflineLocalRepository(
    private val nftContractDao: NFTContractDao,
    private val ethereumAddressDao: EthereumAddressDao
): LocalRepository {
    override suspend fun insertContract(nftContract: NFTContract)
            = nftContractDao.insert(nftContract)

    override fun getContractsStream(): Flow<List<NFTContract>> =
        nftContractDao.getNFTContracts()

    override suspend fun clearContractsTable() {
        nftContractDao.clearContractsTable()
    }

    override suspend fun insertEthereumAddress(ethereumAddress: EthereumAddress) {
        ethereumAddressDao.insertAddress(ethereumAddress)
    }

    override suspend fun clearEthereumAddressTable() {
        ethereumAddressDao.clearAddressTable()
    }
    override suspend fun getEthereumAddress(): Flow<EthereumAddress?> =
        ethereumAddressDao.getEthereumAddress()

}