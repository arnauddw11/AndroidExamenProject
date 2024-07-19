package com.example.androidexamenproject.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.androidexamenproject.ui.viewModel.AlchemyViewModel

@Composable
fun AccountScreen(
    alchemyViewModel: AlchemyViewModel
) {
    val ethereumAddress by alchemyViewModel.ethereumAddress.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Log the length of the Ethereum address for debugging purposes

        // Check if the address is valid and display accordingly
        val displayText = if (ethereumAddress.isEmpty() ||
            !(ethereumAddress.startsWith("0x") || ethereumAddress.endsWith(".eth"))) {
            "Address not available"
        } else {
            ethereumAddress
        }

        Text(text = displayText)
    }
}
