package com.example.androidexamenproject.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.androidexamenproject.NFTApplication
import com.example.androidexamenproject.data.LocalRepository
import com.example.androidexamenproject.model.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.web3j.ens.EnsResolver
import org.web3j.protocol.Web3j
import org.web3j.utils.Convert
import java.io.IOException
import java.math.BigDecimal
import java.util.regex.Pattern

class EthNodeViewModel(
    private val web3: Web3j,
    private val localRepository: LocalRepository,
) : ViewModel() {
    private val _userInfo = MutableStateFlow<UserInfo?>(null)
    val userInfo: StateFlow<UserInfo?> get() = _userInfo

    fun getUserInfo() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val ensResolver = EnsResolver(web3)
                    val inputAddress = localRepository.getEthereumAddress().toString()
                    var ethAddress: String? = null
                    var resolvedAddress: String? = null
                    Log.d("input address", inputAddress)
                    if (isResolved(inputAddress)) {
                        resolvedAddress = inputAddress
                        ethAddress = ensResolver.reverseResolve(inputAddress).toString()
                        Log.d("Resolved eth address", ethAddress.toString())
                    } else if (inputAddress.contains(".eth")) {
                        resolvedAddress = ensResolver.resolve(inputAddress).toString()
                        Log.d("Resolved address", resolvedAddress.toString())
                        ethAddress = inputAddress
                    }
                    Log.d("eth address", inputAddress)
                    //val balance = web3.ethGetBalance(inputAddress, DefaultBlockParameterName.LATEST).send().balance
                    val balance = 3
                    val etherBalance = Convert.fromWei(BigDecimal(balance), Convert.Unit.ETHER)

                    _userInfo.value = UserInfo(ethAddress, resolvedAddress, etherBalance, "")
                    Log.d("userinfo created", UserInfo(ethAddress, resolvedAddress, etherBalance, "").toString())
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

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as NFTApplication)
                val localRepository = application.container.localRepository
                EthNodeViewModel(application.container.web3, localRepository)
            }
        }
    }
}
