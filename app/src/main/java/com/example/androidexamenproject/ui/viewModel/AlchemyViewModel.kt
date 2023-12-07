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
import com.example.androidexamenproject.model.EthereumAddress
import com.example.androidexamenproject.model.NFTContract
import com.example.androidexamenproject.model.NftObject
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import kotlinx.serialization.json.jsonArray
import java.io.IOException
class AlchemyViewModel(
    private val alchemyRepository: AlchemyRepository
) : ViewModel() {

    private val _ethereumAddress = mutableStateOf("")
    val ethereumAddress: State<String> get() = _ethereumAddress

    private val _collectionContractAddress = mutableStateOf("")
    val collectionContractAddress: State<String> get() = _collectionContractAddress

    private val _contractsForOwner = mutableStateOf<List<NFTContract>?>(null)
    val contractsForOwner: State<List<NFTContract>?> get() = _contractsForOwner

    private val _nftsForOwner = mutableStateOf<List<NftObject>?>(null)
    val nftsForOwner: State<List<NftObject>?> get() = _nftsForOwner


    fun setEthereumAddress(address: String) {
        viewModelScope.launch {
            try {
                alchemyRepository.insertEthereumAddress(EthereumAddress(address))

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun setCollectionContractAddress(address: String) {
        _collectionContractAddress.value = address
    }

    fun getContractsForOwner(address: String) {
        viewModelScope.launch {
            try {
                alchemyRepository.getContractsStream().collect { localContracts ->
                    _contractsForOwner.value = localContracts

                    if (localContracts.isEmpty()) {
                        val response = alchemyRepository.getContractsForOwner(address)
                        if (response.isSuccessful) {
                            val contracts = response.body()?.get("contracts")?.jsonArray
                            val nftContractsForOwner = Gson().fromJson<List<NFTContract>>(
                                contracts.toString(),
                                object : TypeToken<List<NFTContract>>() {}.type
                            )
                            for (nftContract in nftContractsForOwner) {
                                alchemyRepository.insertContract(nftContract)
                            }
                            _contractsForOwner.value = nftContractsForOwner
                        }
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
                    Log.d("ownedNfts", ownedNfts.toString())
                        val nfts = Gson().fromJson<List<NftObject>>(
                            ownedNfts.toString(),
                            object : TypeToken<List<NftObject>>() {}.type
                        )
                        Log.d("result", nfts.toString())

                        _nftsForOwner.value = nfts
                    } else {
                        Log.d("json", "JSON is null")
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
                AlchemyViewModel(alchemyRepository = alchemyRepository)
            }
        }
    }
}
