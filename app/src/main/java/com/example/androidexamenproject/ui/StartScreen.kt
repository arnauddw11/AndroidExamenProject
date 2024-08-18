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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.androidexamenproject.ui.viewModel.AlchemyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GiveEthereumAddress(navController: NavController, alchemyViewModel: AlchemyViewModel) {
    var ethereumAddress by remember { mutableStateOf("") }
    var isValidAddress by remember { mutableStateOf(true) }

    /**
     * Validates if the input string is a valid Ethereum address or ENS name.
     *
     * @param address The input address string to validate.
     * @return True if the address is a valid Ethereum address or ENS name, false otherwise.
     */
    fun validateEthereumAddress(address: String): Boolean {
        val ethAddressRegex = "^0x[a-fA-F0-9]{40}\$".toRegex()
        val ensAddressRegex = "^[a-zA-Z0-9-]+\\.eth\$".toRegex()
        return ethAddressRegex.matches(address) || ensAddressRegex.matches(address)
    }

    // UI layout of the screen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title text for the screen
        Text(
            text = "NFT Gallery",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Subtitle text for instructions
        Text(
            text = "Enter your Ethereum address or ENS name to view your NFTs",
            color = Color.Gray,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 18.sp
            )
        )
        Spacer(modifier = Modifier.height(24.dp))

        // TextField for entering Ethereum address or ENS name
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
            label = { Text(text = "Address") },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .fillMaxWidth()
        )

        // Error message for invalid input
        if (!isValidAddress) {
            Text(
                text = "Invalid Ethereum address or ENS name",
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Button to initiate the process of fetching NFTs
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
            Text(text = "Start")
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
