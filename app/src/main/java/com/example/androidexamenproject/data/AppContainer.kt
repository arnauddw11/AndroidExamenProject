package com.example.androidexamenproject.data

import android.content.Context
import com.example.androidexamenproject.network.ApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit


interface AppContainer {
    val alchemyRepository: AlchemyRepository
}

class DefaultAppContainer(context: Context) : AppContainer {

    private val BASE_URL = "https://eth-mainnet.g.alchemy.com/nft/v3//"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    /*
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor) // Add the logging interceptor
        .build()
    */
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        //.client(okHttpClient)
        .baseUrl(BASE_URL)
        .build()


    private val alchemyRetrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    override val alchemyRepository: AlchemyRepository by lazy {
        NetworkAlchemyRepository(alchemyRetrofitService, NFTAppDatabase.getDatabase(context).nftContractDao(), NFTAppDatabase.getDatabase(context).ethereumAddressDao())
    }
}
