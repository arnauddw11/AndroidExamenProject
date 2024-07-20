package com.example.androidexamenproject.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.androidexamenproject.ui.viewModel.AlchemyViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GiveEthereumAddress(navController: NavController, alchemyViewModel: AlchemyViewModel){
    var ethereumAddress by remember { mutableStateOf("") }
    var isValidAddress by remember { mutableStateOf(true) }

    fun validateEthereumAddress(address: String): Boolean {
        val ethAddressRegex = "^0x[a-fA-F0-9]{40}\$".toRegex()
        val ensAddressRegex = "^[a-zA-Z0-9-]+\\.eth\$".toRegex()
        return ethAddressRegex.matches(address) || ensAddressRegex.matches(address)
    }

    LaunchedEffect(ethereumAddress) {
        isValidAddress = validateEthereumAddress(ethereumAddress)
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
            onValueChange = {
                ethereumAddress = it
                isValidAddress = validateEthereumAddress(it)
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            isError = !isValidAddress,
            label = { Text(text = "Address")

            }
        )
        if (!isValidAddress) {
            Text(
                text = "Invalid Ethereum address or ENS name",
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                alchemyViewModel.setEthAddress(ethereumAddress)
                alchemyViewModel.getContractsForOwner(ethereumAddress)
                navController.navigate("collections")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = ethereumAddress.isNotEmpty() && isValidAddress
        ) {
            Text(text = "Confirm")
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}