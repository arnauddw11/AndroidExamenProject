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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.jsonArray
import org.web3j.ens.EnsResolver
import org.web3j.protocol.Web3j
import java.io.IOException
import java.util.regex.Pattern

class AlchemyViewModel(
    private val alchemyRepository: AlchemyRepository,
    private val localRepository: LocalRepository,
    private val web3j: Web3j
) : ViewModel() {

    private val _ethereumAddress = MutableStateFlow("")
    val ethereumAddress: StateFlow<String> get() = _ethereumAddress

    private val _collectionContractAddress = mutableStateOf("")
    val collectionContractAddress: State<String> get() = _collectionContractAddress

    private val _contractsForOwner = MutableStateFlow<List<NFTContract>?>(null)
    val contractsForOwner: StateFlow<List<NFTContract>?> get() = _contractsForOwner

    private val _nftsForOwner = mutableStateOf<List<NftObject>?>(null)
    val nftsForOwner: State<List<NftObject>?> get() = _nftsForOwner

    private val _rarities = MutableStateFlow<List<Rarity>?>(null)
    val rarities: StateFlow<List<Rarity>?> get() = _rarities

    fun getEthereumAddress() {
        viewModelScope.launch {
            try {
                _ethereumAddress.value = localRepository.getEthereumAddress().toString()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

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

    private fun isResolved(address: String): Boolean {
        val ethAddressPattern = Pattern.compile("^0x[a-fA-F0-9]{40}$")
        return ethAddressPattern.matcher(address).matches()
    }

    private suspend fun getEnsName(address: String): String {
        return withContext(Dispatchers.IO) {
            try {
                if (isResolved(address)) {
                    EnsResolver(web3j).reverseResolve(address)
                } else {
                    address
                }
            } catch (e: Exception) {
                Log.e("ENS Resolution", "Failed to resolve ENS name", e)
                address
            }
        }
    }

    private suspend fun getResolvedAddress(address: String): String {
        return withContext(Dispatchers.IO) {
            try {
                if (isResolved(address)) {
                    address
                } else {
                    EnsResolver(web3j).resolve(address)
                }
            } catch (e: Exception) {
                Log.e("ENS Resolution", "Failed to resolve address", e)
                address
            }
        }
    }

    fun setCollectionContractAddress(address: String) {
        _collectionContractAddress.value = address
    }

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
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as NFTApplication)
                val alchemyRepository = application.container.alchemyRepository
                val localRepository = application.container.localRepository
                AlchemyViewModel(alchemyRepository = alchemyRepository, localRepository = localRepository, web3j = application.container.web3j)
            }
        }
    }
}
