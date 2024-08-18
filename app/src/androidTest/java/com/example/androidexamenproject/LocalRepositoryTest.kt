package com.example.androidexamenproject

import com.example.androidexamenproject.data.EthereumAddressDao
import com.example.androidexamenproject.data.NFTContractDao
import com.example.androidexamenproject.data.OfflineLocalRepository
import com.example.androidexamenproject.model.EthereumAddress
import com.example.androidexamenproject.model.NFTContract
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class LocalRepositoryTest {
    // Mocked dependencies
    private lateinit var nftContractDao: NFTContractDao
    private lateinit var ethereumAddressDao: EthereumAddressDao

    // Repository under test
    private lateinit var localRepository: OfflineLocalRepository

    @Before
    fun setup() {
        // Initialize mocked dependencies
        nftContractDao = mock()
        ethereumAddressDao = mock()

        // Initialize the repository with the mocked DAOs
        localRepository = OfflineLocalRepository(nftContractDao, ethereumAddressDao)
    }

    @Test
    fun insertContract_test() = runBlocking {
        // Arrange
        val nftContract = NFTContract(address = "0xfa", name = "TestContract", totalSupply = 10000, openSeaMetadata = null, totalBalance = "1000", numDistinctTokensOwned = "100", displayNft = null, image = null)

        // Act
        localRepository.insertContract(nftContract)

        // Assert
        verify(nftContractDao).insert(nftContract)
    }

    @Test
    fun getContractsStream_test() = runBlocking {
        // Arrange
        val expectedContracts = listOf(NFTContract(address = "0xfa", name = "TestContract", totalSupply = 10000, openSeaMetadata = null, totalBalance = "1000", numDistinctTokensOwned = "100", displayNft = null, image = null) )
        whenever(nftContractDao.getNFTContracts()).thenReturn(flowOf(expectedContracts))

        // Act
        val contracts = localRepository.getContractsStream()

        // Assert
        contracts.collect {
            assertEquals(expectedContracts, it)
        }
    }

    @Test
    fun clearContractsTable_test() = runBlocking {
        // Act
        localRepository.clearContractsTable()

        // Assert
        verify(nftContractDao).clearContractsTable()
    }

    @Test
    fun insertEthereumAddress_test() = runBlocking {
        // Arrange
        val ethereumAddress = EthereumAddress(ethAddress = "0x123", ensAddress = "test.eth")

        // Act
        localRepository.insertEthereumAddress(ethereumAddress)

        // Assert
        verify(ethereumAddressDao).insertAddress(ethereumAddress)
    }

    @Test
    fun getEthereumAddress_test() = runBlocking {
        // Arrange
        val expectedAddress = EthereumAddress(ethAddress = "0x123", ensAddress = "test.eth")
        whenever(ethereumAddressDao.getEthereumAddress()).thenReturn(flowOf(expectedAddress))

        // Act
        val ethereumAddress = localRepository.getEthereumAddress()

        // Assert
        ethereumAddress.collect {
            assertEquals(expectedAddress, it)
        }
    }

    @Test
    fun clearEthereumAddressTable_test() = runBlocking {
        // Act
        localRepository.clearEthereumAddressTable()

        // Assert
        verify(ethereumAddressDao).clearAddressTable()
    }

    @Test
    fun insertAvatar_test() = runBlocking {
        // Arrange
        val ethAddress = "0x123"
        val avatar = "avatarUrl"

        // Act
        localRepository.insertAvatar(ethAddress, avatar)

        // Assert
        verify(ethereumAddressDao).updateAvatar(ethAddress, avatar)
    }

    @Test
    fun getAvatar_test() = runBlocking {
        // Arrange
        val expectedAvatar = "avatarUrl"
        whenever(ethereumAddressDao.getAvatar()).thenReturn(flowOf(expectedAvatar))

        // Act
        val avatar = localRepository.getAvatar()

        // Assert
        avatar.collect {
            assertEquals(expectedAvatar, it)
        }
    }
}
