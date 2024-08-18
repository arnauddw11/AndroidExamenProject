package com.example.androidexamenproject.data

import android.content.Context
import com.example.androidexamenproject.network.ApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.kethereum.rpc.EthereumRPC
import org.kethereum.rpc.HttpEthereumRPC
import retrofit2.Retrofit


/**
 * AppContainer is an interface that provides dependencies for the application.
 * It includes repositories and services necessary for interacting with the Ethereum blockchain and local storage.
 */
interface AppContainer {
    /**
     * Provides access to the AlchemyRepository for interacting with the Alchemy API.
     */
    val alchemyRepository: AlchemyRepository

    /**
     * Provides access to the LocalRepository for managing local data related to NFT contracts and Ethereum addresses.
     */
    val localRepository: LocalRepository

    /**
     * Provides access to the EthereumRPC client for making Ethereum blockchain RPC calls.
     */
    val ethereumRPC: EthereumRPC
}

/**
 * DefaultAppContainer is the default implementation of the AppContainer interface.
 * It initializes and provides dependencies such as repositories and services for the application.
 *
 * @param context The application context used for initializing the database and other services.
 */
class DefaultAppContainer(context: Context) : AppContainer {

    // Base URL for the Alchemy API
    private val BASE_URL = "https://eth-mainnet.g.alchemy.com/nft/v3/XDJKhrYm6fHJodk3E0sXUOt_YpgePsdO/"

    // Ethereum node URL for making RPC calls
    private val ETH_NODE_URL = "https://eth-mainnet.g.alchemy.com/v2/XDJKhrYm6fHJodk3E0sXUOt_YpgePsdO"

    // Retrofit instance for making network requests to the Alchemy API
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()

    /**
     * EthereumRPC client for making Ethereum blockchain RPC calls.
     */
    override val ethereumRPC: EthereumRPC = HttpEthereumRPC(ETH_NODE_URL)

    // Lazy initialization of the ApiService for the Alchemy API
    private val alchemyRetrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    /**
     * AlchemyRepository for interacting with the Alchemy API.
     */
    override val alchemyRepository: AlchemyRepository by lazy {
        NetworkAlchemyRepository(alchemyRetrofitService)
    }

    /**
     * LocalRepository for managing local data related to NFT contracts and Ethereum addresses.
     */
    override val localRepository: LocalRepository by lazy {
        OfflineLocalRepository(
            NFTAppDatabase.getDatabase(context).nftContractDao(),
            NFTAppDatabase.getDatabase(context).ethereumAddressDao()
        )
    }
}
