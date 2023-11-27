package com.example.androidexamenproject.ui.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.androidexamenproject.NFTApplication
import com.example.androidexamenproject.data.AlchemyRepository
import com.example.androidexamenproject.model.NFTCollection
import com.example.androidexamenproject.model.NFTContract
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import kotlinx.serialization.json.jsonArray
import java.io.IOException
import java.net.Inet4Address

class AlchemyViewModel(private val alchemyRepository: AlchemyRepository) : ViewModel() {
    private val _collectionsForOwner = mutableStateOf<List<NFTCollection>?>(null)
    val collectionsForOwner: State<List<NFTCollection>?> get() = _collectionsForOwner

    private val _nftsForOwner = mutableStateOf<List<NFTContract>?>(null)
    val nftsForOwner: State<List<NFTContract>?> get() = _nftsForOwner

    fun getCollectionForOwner(address: String) {
        viewModelScope.launch {
            try {
                val response = alchemyRepository.getCollectionsForOwner(address)
                if (response.isSuccessful) {
                    val collections = response.body()?.get("collections")?.jsonArray
                    val nftCollectionsForOwner = Gson().fromJson<List<NFTCollection>>(
                        collections.toString(),
                        object : TypeToken<List<NFTCollection>>() {}.type
                    )
                    _collectionsForOwner.value = nftCollectionsForOwner
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun getNftsForOwner(owner: String, contractAddress: String) {
        viewModelScope.launch {
            try {
                val response = alchemyRepository.getNFTsForOwner(owner, contractAddress)
                if (response.isSuccessful) {
                    val nfts = response.body()?.get("ownedNfts")?.jsonArray
                    val nftContractsForOwner = Gson().fromJson<List<NFTContract>>(
                        nfts.toString(),
                        object : TypeToken<List<NFTContract>>() {}.type
                    )
                    _nftsForOwner.value = nftContractsForOwner
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
