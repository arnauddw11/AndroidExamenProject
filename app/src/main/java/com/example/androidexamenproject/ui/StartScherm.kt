package com.example.androidexamenproject.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.androidexamenproject.ui.viewModel.AlchemyViewModel



@Composable
fun DisplayNFTCollectionInfo(alchemyViewModel: AlchemyViewModel) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        alchemyViewModel.collectionsForOwner.value?.let { collections ->
            for (collection in collections) {
                // if not property is not null, show the property
                if (collection.name != null) {
                    Text(text = collection.name)
                }
                if (collection.slug != null) {
                    Text(text = collection.slug)
                }
                if (collection.description != null) {
                    Text(text = collection.description)
                }
                if (collection.image != null) {
                    Text(text = collection.image?.pngUrl.toString())
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GiveEthereumAddress(navController: NavController, alchemyViewModel: AlchemyViewModel){
    var ethereumAddress by remember { mutableStateOf("") }
    DisposableEffect(Unit) {
        alchemyViewModel.getCollectionForOwner(ethereumAddress)
        onDispose { }
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = ethereumAddress,
            onValueChange = { ethereumAddress = it},
            label = { Text(text = "Address")
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                alchemyViewModel.getCollectionForOwner(ethereumAddress)
                navController.navigate("collections")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Confirm")
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}