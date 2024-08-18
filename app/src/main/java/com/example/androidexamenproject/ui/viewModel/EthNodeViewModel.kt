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



/**
 * ViewModel for managing Ethereum-related user information, including balance, ENS avatars, and NFT metadata.
 *
 * @param ethereumRPC The Ethereum RPC client for interacting with the Ethereum blockchain.
 * @param localRepository The repository for local data storage and retrieval.
 * @param alchemyRepository The repository for interacting with the Alchemy API.
 */
class EthNodeViewModel(
    private val ethereumRPC: EthereumRPC,
    private val localRepository: LocalRepository,
    private val alchemyRepository: AlchemyRepository
) : ViewModel() {

    // Holds the user information including ENS name, Ethereum address, balance, and avatar.
    private val _userInfo = MutableStateFlow<UserInfo?>(null)
    val userInfo: StateFlow<UserInfo?> get() = _userInfo

    /**
     * Retrieves the user information, including Ethereum balance, ENS avatar, and stores it in the local repository.
     */
    fun getUserInfo() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val ethAddress = localRepository.getEthereumAddress().firstOrNull()
                    ethAddress?.let { address ->

                        val balance = ethereumRPC.getBalance(Address(address.ethAddress), "latest")

                        val avatar = getAvatar(address)
                        localRepository.insertAvatar(address.ethAddress, avatar)
                        _userInfo.value = UserInfo(
                            address.ensAddress,
                            address.ethAddress,
                            formatBalance(balance as BigInteger),
                            avatar
                        )
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Formats the Ethereum balance from Wei to Ether and rounds it to two decimal places.
     *
     * @param balance The balance in Wei as a BigInteger.
     * @return The formatted balance in Ether as a Double.
     */
    private fun formatBalance(balance: BigInteger): Double {
        val gweiInEther = BigInteger("1000000000") // 10^9
        val etherValue = balance.toDouble() / gweiInEther.toDouble()
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.DOWN
        return df.format(etherValue).toDouble()
    }

    /**
     * Retrieves the ENS avatar URL for the given Ethereum address, resolving IPFS or EIP-155 based avatars.
     *
     * @param address The Ethereum address object containing the ENS name.
     * @return The resolved avatar URL as a String.
     */
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
        /**
         * Factory for creating an instance of EthNodeViewModel.
         */
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as NFTApplication)
                val localRepository = application.container.localRepository
                EthNodeViewModel(
                    application.container.ethereumRPC,
                    localRepository,
                    application.container.alchemyRepository
                )
            }
        }
    }
}
