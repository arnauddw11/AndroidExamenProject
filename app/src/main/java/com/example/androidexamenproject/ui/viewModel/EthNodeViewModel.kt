package com.example.androidexamenproject.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.androidexamenproject.NFTApplication
import com.example.androidexamenproject.data.AlchemyRepository
import com.example.androidexamenproject.data.LocalRepository
import com.example.androidexamenproject.model.EthereumAddress
import com.example.androidexamenproject.model.NftMetadata
import com.example.androidexamenproject.model.UserInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.jsonObject
import org.kethereum.eip137.model.ENSName
import org.kethereum.ens.ENS
import org.kethereum.model.Address
import org.kethereum.rpc.EthereumRPC
import java.io.IOException
import java.math.BigInteger
import java.math.RoundingMode
import java.text.DecimalFormat



class EthNodeViewModel(
    private val ethereumRPC: EthereumRPC,
    private val localRepository: LocalRepository,
    private val alchemyRepository: AlchemyRepository
) : ViewModel() {
    private val _userInfo = MutableStateFlow<UserInfo?>(null)
    val userInfo: StateFlow<UserInfo?> get() = _userInfo

    fun getUserInfo() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val ethAddress = localRepository.getEthereumAddress().firstOrNull()
                    ethAddress?.let { address ->

                        var balance = ethereumRPC.getBalance(Address(address.ethAddress), "latest")

                        val ens = ENS(ethereumRPC)
                        val avatar = getAvatar(address)
                        _userInfo.value = UserInfo(address.ensAddress, address.ethAddress, formatBalance(balance as BigInteger), avatar)
                    }
                }
                Log.d("test userInfo", userInfo.value.toString())
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }
    private fun formatBalance(balance: BigInteger): Double {
        val gweiInEther = BigInteger("1000000000") // 10^9
        val etherValue = balance.toDouble() / gweiInEther.toDouble()
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.DOWN
        return df.format(etherValue).toDouble()
    }

    private suspend fun getAvatar(address: EthereumAddress): String {
        val ens = ENS(ethereumRPC)
        val avatar = ens.getAvatar(ENSName(address.ensAddress.toString())).toString()
        Log.d("getAvatar", "ENS avatar address: $avatar")

        return when {
            avatar.startsWith("ipfs://") -> {
                val gatewayUrl = "https://ipfs.io/ipfs/${avatar.removePrefix("ipfs://")}"
                Log.d("getAvatar", "IPFS gateway URL: $gatewayUrl")
                gatewayUrl
            }
            avatar.startsWith("eip155:1") -> {
                val parts = avatar.split(":")[2].split("/")
                val contractAddress = parts[0]
                val tokenId = parts[1]
                Log.d("getAvatar", "Contract address: $contractAddress, Token ID: $tokenId")

                val response = alchemyRepository.getNFTMetadata(contractAddress, tokenId)
                Log.d("getAvatar", "Alchemy response: $response")

                if (response.isSuccessful) {
                    val contractJson = response.body()?.jsonObject
                    Log.d("getAvatar", "Contract JSON: $contractJson")

                    contractJson?.let {
                        val nft = Gson().fromJson<NftMetadata>(
                            it.toString(),
                            object : TypeToken<NftMetadata>() {}.type
                        )
                        Log.d("getAvatar", "NFT metadata: ${nft.image?.toString() ?: "contract is null"}")
                        return nft.image?.pngUrl ?: avatar
                    } ?: run {
                        Log.e("getAvatar", "Contract JSON is null")
                        return avatar
                    }
                } else {
                    Log.e("getAvatar", "Response unsuccessful: ${response.errorBody()}")
                    return avatar
                }
            }
            else -> avatar
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as NFTApplication)
                val localRepository = application.container.localRepository
                EthNodeViewModel(application.container.ethereumRPC, localRepository, application.container.alchemyRepository)
            }
        }
    }
}
