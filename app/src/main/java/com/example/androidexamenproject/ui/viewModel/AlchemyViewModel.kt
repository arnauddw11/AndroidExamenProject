package com.example.androidexamenproject.ui.viewModel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.androidexamenproject.NFTApplication
import com.example.androidexamenproject.data.AlchemyRepository
import com.example.androidexamenproject.data.LocalRepository
import com.example.androidexamenproject.model.EthereumAddress
import com.example.androidexamenproject.model.NFTContract
import com.example.androidexamenproject.model.NftObject
import com.example.androidexamenproject.model.Rarity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.jsonArray
import org.kethereum.eip137.model.ENSName
import org.kethereum.ens.ENS
import org.kethereum.model.Address
import org.kethereum.rpc.EthereumRPC
import java.io.IOException
import java.util.regex.Pattern

/**
 * ViewModel for managing Ethereum addresses, NFTs, and contract details.
 *
 * @param alchemyRepository The repository for interacting with the Alchemy API.
 * @param localRepository The repository for local data storage and retrieval.
 * @param ethereumRPC The Ethereum RPC client for interacting with the Ethereum blockchain.
 */
class AlchemyViewModel(
    private val alchemyRepository: AlchemyRepository,
    private val localRepository: LocalRepository,
    private val ethereumRPC: EthereumRPC
) : ViewModel() {

    // Holds the current Ethereum address.
    private val _ethereumAddress = MutableStateFlow("")
    val ethereumAddress: StateFlow<String> get() = _ethereumAddress

    // Holds the details of the Ethereum address, such as ENS name.
    private val _ethDetails = MutableStateFlow<EthereumAddress?>(null)
    val ethDetails: StateFlow<EthereumAddress?> get() = _ethDetails

    // Holds the contract address for a specific NFT collection.
    private val _collectionContractAddress = mutableStateOf("")
    val collectionContractAddress: State<String> get() = _collectionContractAddress

    // Holds the list of NFT contracts owned by the user.
    private val _contractsForOwner = MutableStateFlow<List<NFTContract>?>(null)
    val contractsForOwner: StateFlow<List<NFTContract>?> get() = _contractsForOwner

    // Holds the list of NFTs owned by the user.
    private val _nftsForOwner = mutableStateOf<List<NftObject>?>(null)
    val nftsForOwner: State<List<NftObject>?> get() = _nftsForOwner

    // Holds the list of rarities for a specific NFT.
    private val _rarities = MutableStateFlow<List<Rarity>?>(null)
    val rarities: StateFlow<List<Rarity>?> get() = _rarities

    /**
     * Retrieves the stored Ethereum address from the local repository and updates the state.
     */
    fun getEthereumAddress() {
        viewModelScope.launch {
            try {
                var ethAddress = localRepository.getEthereumAddress().firstOrNull()
                _ethereumAddress.value = ethAddress?.ethAddress.toString()
                _ethDetails.value = ethAddress
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Sets the Ethereum address, resolves it to an ENS name if necessary,
     * and updates the local repository with the new address details.
     *
     * @param address The Ethereum address or ENS name to set.
     */
    fun setEthAddress(address: String) {
        viewModelScope.launch {
            try {
                val resolvedAddress = withContext(Dispatchers.IO) { getResolvedAddress(address) }
                val ensName = withContext(Dispatchers.IO) { getEnsName(address) }
                if (localRepository.getEthereumAddress().equals("")) {
                    localRepository.insertEthereumAddress(EthereumAddress(resolvedAddress, ensName))
                } else {
                    localRepository.clearEthereumAddressTable()
                    localRepository.insertEthereumAddress(EthereumAddress(resolvedAddress, ensName))
                    _ethereumAddress.value = address
                    localRepository.clearContractsTable()
                    _contractsForOwner.value = null
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Checks if the provided address is already resolved (i.e., a valid Ethereum address).
     *
     * @param address The address to check.
     * @return True if the address is resolved, false otherwise.
     */
    private fun isResolved(address: String): Boolean {
        val ethAddressPattern = Pattern.compile("^0x[a-fA-F0-9]{40}$")
        return ethAddressPattern.matcher(address).matches()
    }

    /**
     * Resolves the Ethereum address to an ENS name if possible.
     *
     * @param address The Ethereum address to resolve.
     * @return The ENS name if resolved, otherwise returns the original address.
     */
    private fun getEnsName(address: String): String {
        return try {
            if (isResolved(address)) {
                val ens = ENS(ethereumRPC)
                val address = ens.reverseResolve(Address(address))
                address.toString()
            } else {
                address
            }
        } catch (e: Exception) {
            Log.e("ENS Resolution", "Failed to resolve ENS name", e)
            address
        }
    }

    /**
     * Resolves an ENS name to an Ethereum address, or returns the original address if already resolved.
     *
     * @param address The ENS name or Ethereum address to resolve.
     * @return The resolved Ethereum address or the original address.
     */
    private suspend fun getResolvedAddress(address: String): String {
        return withContext(Dispatchers.IO) {
            try {
                if (isResolved(address)) {
                    address
                } else {
                    val ens = ENS(ethereumRPC)
                    val resolvedAddress = ens.getAddress(ENSName(address))
                    resolvedAddress.toString()
                }
            } catch (e: Exception) {
                Log.e("ENS Resolution", "Failed to resolve address", e)
                address
            }
        }
    }

    /**
     * Sets the contract address for a specific NFT collection.
     *
     * @param address The contract address to set.
     */
    fun setCollectionContractAddress(address: String) {
        _collectionContractAddress.value = address
    }

    /**
     * Retrieves the list of NFT contracts owned by the specified address from the Alchemy API
     * and updates the state and local repository.
     *
     * @param address The Ethereum address to retrieve contracts for.
     */
    fun getContractsForOwner(address: String) {
        viewModelScope.launch {
            try {
                if (contractsForOwner.value.isNullOrEmpty()) {
                    val response = alchemyRepository.getContractsForOwner(address)
                    if (response.isSuccessful) {
                        val contracts = response.body()?.get("contracts")?.jsonArray
                        val nftContractsForOwner = Gson().fromJson<List<NFTContract>>(
                            contracts.toString(),
                            object : TypeToken<List<NFTContract>>() {}.type
                        )
                        for (nftContract in nftContractsForOwner) {
                            localRepository.insertContract(nftContract)
                        }
                        _contractsForOwner.value = nftContractsForOwner
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Retrieves the list of NFTs owned by the specified address for the given contract addresses
     * from the Alchemy API and updates the state.
     *
     * @param address The Ethereum address to retrieve NFTs for.
     * @param contractAddresses The list of contract addresses to retrieve NFTs from.
     */
    fun getNFTsForOwner(address: String, contractAddresses: List<String>) {
        viewModelScope.launch {
            try {
                val response = alchemyRepository.getNFtsForOwner(address, contractAddresses)
                if (response.isSuccessful) {
                    val ownedNfts = response.body()?.get("ownedNfts")?.jsonArray
                    val nfts = Gson().fromJson<List<NftObject>>(
                        ownedNfts.toString(),
                        object : TypeToken<List<NftObject>>() {}.type
                    )
                    _nftsForOwner.value = nfts
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Computes the rarity of a specific NFT by its contract address and token ID,
     * and updates the state with the computed rarities.
     *
     * @param contractAddress The contract address of the NFT.
     * @param tokenId The token ID of the NFT.
     */
    fun computeRarity(contractAddress: String, tokenId: String) {
        viewModelScope.launch {
            try {
                val response = alchemyRepository.computeRarity(contractAddress, tokenId)
                if (response.isSuccessful) {
                    val raritiesJSON = response.body()?.get("rarities")?.jsonArray
                    val rarities = Gson().fromJson<List<Rarity>>(
                        raritiesJSON.toString(),
                        object : TypeToken<List<Rarity>>() {}.type
                    )
                    _rarities.value = rarities
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        /**
         * Factory for creating an instance of AlchemyViewModel.
         */
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as NFTApplication)
                val alchemyRepository = application.container.alchemyRepository
                val localRepository = application.container.localRepository
                AlchemyViewModel(alchemyRepository = alchemyRepository, localRepository = localRepository, ethereumRPC = application.container.ethereumRPC)
            }
        }
    }
}

