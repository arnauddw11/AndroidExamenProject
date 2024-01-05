package com.example.androidexamenproject

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.androidexamenproject.data.NetworkAlchemyRepository
import com.example.androidexamenproject.fake.FakeDataSource
import com.example.androidexamenproject.fake.FakeNFTCollectionsApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Test
    fun getContractsForOwnerTest() =
        runTest {
            val repository = NetworkAlchemyRepository(FakeNFTCollectionsApi())
            val response =
                repository.getContractsForOwner("0x0d4c98901563ca9fb7bdaa3c3a4ebee2b6c65dd4")
            val expected = Json.encodeToJsonElement(FakeDataSource.fakeNfts).jsonObject

            Log.d("expected", expected.toString())
            Log.d("response", response.body().toString())
            assertEquals(expected.toString(), response.body().toString())
        }

}