package com.example.androidexamenproject.data

import android.content.Context
import com.example.androidexamenproject.network.ApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import retrofit2.Retrofit


interface AppContainer {
    val alchemyRepository: AlchemyRepository
    val localRepository: LocalRepository
    val web3: Web3j
}

class DefaultAppContainer(context: Context) : AppContainer {

    private val BASE_URL = "https://eth-mainnet.g.alchemy.com/nft/v3/XDJKhrYm6fHJodk3E0sXUOt_YpgePsdO/"
    private val ETH_NODE_URL = "https://eth-mainnet.g.alchemy.com/v2/XDJKhrYm6fHJodk3E0sXUOt_YpgePsdO"

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()

    override val web3 = Web3j.build(HttpService(ETH_NODE_URL))


    private val alchemyRetrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    override val alchemyRepository: AlchemyRepository by lazy {
        NetworkAlchemyRepository(alchemyRetrofitService)
    }
    override val localRepository: LocalRepository by lazy {
        OfflineLocalRepository(NFTAppDatabase.getDatabase(context).nftContractDao(), NFTAppDatabase.getDatabase(context).ethereumAddressDao())
    }
}
