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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.androidexamenproject.ui.viewModel.AlchemyViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GiveEthereumAddress(navController: NavController, alchemyViewModel: AlchemyViewModel){
    var ethereumAddress by remember { mutableStateOf("") }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = ethereumAddress,
            onValueChange = { ethereumAddress = it },
            label = { Text(text = "Address")
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                alchemyViewModel.setEthaddress(ethereumAddress)
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