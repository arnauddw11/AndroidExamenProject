package com.example.androidexamenproject.data

import com.example.androidexamenproject.network.ApiService
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.JsonObject
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import retrofit2.Response

class AlchemyRepositoryTest {

    // Mocked dependency
    private lateinit var apiService: ApiService

    // Repository under test
    private lateinit var alchemyRepository: NetworkAlchemyRepository

    @Before
    fun setup() {
        // Initialize mocked dependency
        apiService = mock()

        // Initialize the repository with the mocked ApiService
        alchemyRepository = NetworkAlchemyRepository(apiService)
    }

    @Test
    fun getContractsForOwner_test() {
        runBlocking {
            // Arrange
            val owner = "0x123"
            val expectedResponse = Response.success(JsonObject(mapOf("contracts" to JsonObject(mapOf()))))
            whenever(apiService.getContractsForOwner(owner)).thenReturn(expectedResponse)

            // Act
            val response = alchemyRepository.getContractsForOwner(owner)

            // Assert
            assertEquals(expectedResponse, response)

            verify(apiService).getContractsForOwner(owner)
        }
    }

    @Test
    fun getNFTsForOwner_test() {
        runBlocking {
            // Arrange
            val owner = "0x123"
            val contractAddresses = listOf("0xabc", "0xdef")
            val expectedResponse = Response.success(JsonObject(mapOf("nfts" to JsonObject(mapOf()))))
            whenever(apiService.getNFTsForOwner(owner, contractAddresses)).thenReturn(expectedResponse)

            // Act
            val response = alchemyRepository.getNFtsForOwner(owner, contractAddresses)

            // Assert
            assertEquals(expectedResponse, response)
            verify(apiService).getNFTsForOwner(owner, contractAddresses)
        }
    }

    @Test
    fun computeRarity_test() {
        runBlocking {
            // Arrange
            val contractAddress = "0xabc"
            val tokenId = "1"
            val expectedResponse = Response.success(JsonObject(mapOf("rarity" to JsonObject(mapOf()))))
            whenever(apiService.computeRarity(contractAddress, tokenId)).thenReturn(expectedResponse)

            // Act
            val response = alchemyRepository.computeRarity(contractAddress, tokenId)

            // Assert
            assertEquals(expectedResponse, response)
            verify(apiService).computeRarity(contractAddress, tokenId)
        }
    }

    @Test
    fun getNFTMetadata_test() {
        runBlocking {
            // Arrange
            val contractAddress = "0xabc"
            val tokenId = "1"
            val expectedResponse = Response.success(JsonObject(mapOf("metadata" to JsonObject(mapOf()))))
            whenever(apiService.getNFTMetadata(contractAddress, tokenId)).thenReturn(expectedResponse)

            // Act
            val response = alchemyRepository.getNFTMetadata(contractAddress, tokenId)

            // Assert
            assertEquals(expectedResponse, response)
            verify(apiService).getNFTMetadata(contractAddress, tokenId)
        }
    }
}
