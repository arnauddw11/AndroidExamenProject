package com.example.androidexamenproject.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.androidexamenproject.ui.viewModel.AlchemyViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GiveEthereumAddress(navController: NavController, alchemyViewModel: AlchemyViewModel){
    val ethereumAddress by alchemyViewModel.ethereumAddress
    var context =LocalContext.current.applicationContext;
    DisposableEffect(Unit) {
        alchemyViewModel.setEthereumAddress(ethereumAddress)
        alchemyViewModel.getContractsForOwner(ethereumAddress)
        onDispose {
        }
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
            onValueChange = { alchemyViewModel.setEthereumAddress(it) },
            label = { Text(text = "Address")
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                alchemyViewModel.getContractsForOwner(ethereumAddress)
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