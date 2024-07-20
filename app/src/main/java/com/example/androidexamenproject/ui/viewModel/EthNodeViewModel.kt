package com.example.androidexamenproject.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.androidexamenproject.NFTApplication
import com.example.androidexamenproject.data.LocalRepository
import com.example.androidexamenproject.model.EthereumAddress
import com.example.androidexamenproject.model.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.utils.Convert
import java.io.IOException
import java.math.BigDecimal

class EthNodeViewModel(
    private val web3j: Web3j,
    private val localRepository: LocalRepository,
) : ViewModel() {
    private val _ethereumAddress = MutableStateFlow<EthereumAddress?>(null)
    val ethereumAddress: StateFlow<EthereumAddress?> get() = _ethereumAddress
    private val _userInfo = MutableStateFlow<UserInfo?>(null)
    val userInfo: StateFlow<UserInfo?> get() = _userInfo

    fun getUserInfo() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val ethAddress = localRepository.getEthereumAddress().firstOrNull()
                    ethAddress?.let { address ->
                        val balance = web3j.ethGetBalance(address.ethAddress, DefaultBlockParameterName.LATEST).send().balance
                        val etherBalance = Convert.fromWei(BigDecimal(balance), Convert.Unit.ETHER)
                        _userInfo.value = UserInfo(address.ensAddress, address.ethAddress, etherBalance, "")
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun getBalanceByAddress(address: String): BigDecimal {
        return BigDecimal(45456778885)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as NFTApplication)
                val localRepository = application.container.localRepository
                EthNodeViewModel(application.container.web3j, localRepository)
            }
        }
    }
}
