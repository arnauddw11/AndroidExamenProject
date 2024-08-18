package com.example.androidexamenproject

import android.util.Log
import com.example.androidexamenproject.data.AlchemyRepository
import com.example.androidexamenproject.data.LocalRepository
import com.example.androidexamenproject.data.NetworkAlchemyRepository
import com.example.androidexamenproject.data.OfflineLocalRepository
import com.example.androidexamenproject.model.EthereumAddress
import com.example.androidexamenproject.ui.viewModel.EthNodeViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.kethereum.rpc.EthereumRPC
import org.kethereum.rpc.HttpEthereumRPC
import org.mockito.kotlin.mock

class EthNodeViewModelTest {
    private val ETH_NODE_URL = "https://eth-mainnet.g.alchemy.com/v2/XDJKhrYm6fHJodk3E0sXUOt_YpgePsdO"
    private val localRepository: LocalRepository = OfflineLocalRepository(nftContractDao = mock(), ethereumAddressDao = mock())
    private val alchemyRepository: AlchemyRepository = NetworkAlchemyRepository(alchemyApiService = mock())
    private val ethereumRPC: EthereumRPC = HttpEthereumRPC(ETH_NODE_URL)
    private val viewModel = EthNodeViewModel(localRepository = localRepository, alchemyRepository = alchemyRepository, ethereumRPC = ethereumRPC)

    @Test
    fun ethNodeViewModel_getUserInfo(): Unit = runBlocking {
        localRepository.insertEthereumAddress(EthereumAddress("0x73147F1A2EBCf284b2D0061299bdA8608fe0177F", "bonko.eth"))
        viewModel.getUserInfo()
        Log.d("UserInfo", viewModel.userInfo.value.toString())
    }

}