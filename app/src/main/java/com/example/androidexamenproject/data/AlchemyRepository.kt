package com.example.androidexamenproject.data

import com.example.androidexamenproject.model.EthereumAddress
import com.example.androidexamenproject.model.NFTContract
import com.example.androidexamenproject.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonObject
import retrofit2.Response


interface AlchemyRepository {
    suspend fun insertContract(nftContracts: NFTContract)
    suspend fun insertEthereumAddress(ethereumAddress: EthereumAddress)
    fun getEthereumAddress(): Flow<List<EthereumAddress>>
    fun getContractsStream(): Flow<List<NFTContract>>
    suspend fun getContractsForOwner(owner: String): Response<JsonObject>
    suspend fun getNFtsForOwner(owner: String, contractAddresses: List<String>): Response<JsonObject>
}

class NetworkAlchemyRepository(
    private val alchemyApiService: ApiService,
    private val nftContractDao: NFTContractDao,
    private val ethereumAddressDao: EthereumAddressDao
) : AlchemyRepository {
    override suspend fun getContractsForOwner(owner: String): Response<JsonObject> =
        alchemyApiService.getContractsForOwner(owner)

    override suspend fun getNFtsForOwner(owner: String, contractAddresses: List<String>): Response<JsonObject> =
        alchemyApiService.getNFTsForOwner(owner, contractAddresses)

    override suspend fun insertContract(nftContract: NFTContract)
    = nftContractDao.insert(nftContract)

    override fun getContractsStream(): Flow<List<NFTContract>> =
        nftContractDao.getNFTContracts()

    override suspend fun insertEthereumAddress(ethereumAddress: EthereumAddress) =
        ethereumAddressDao.insert(ethereumAddress)

    override fun getEthereumAddress(): Flow<List<EthereumAddress>> =
        ethereumAddressDao.getEthereumAddress()
}




