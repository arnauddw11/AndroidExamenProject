package com.example.androidexamenproject.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.androidexamenproject.ui.viewModel.AlchemyViewModel
import com.example.androidexamenproject.ui.viewModel.EthNodeViewModel

@Composable
fun AccountScreen(
    alchemyViewModel: AlchemyViewModel,
    ethNodeViewModel: EthNodeViewModel
) {
    val userInfo by ethNodeViewModel.userInfo.collectAsState()
    LaunchedEffect(userInfo) {
        ethNodeViewModel.getUserInfo()
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Display user info if available
        userInfo?.let { info ->
            val gatewayUrl = "https://ipfs.io/ipfs/${info.avatar?.removePrefix("ipfs://")}"

            // Convert BigInteger balance to BigDecimal and format it
            Log.d("test etherBalance", info.etherBalance.toString())

            Text(text = info.ensAddress.toString())
            Text(text = info.resolvedAddress.toString())
            AsyncImage(
                model = gatewayUrl,
                contentDescription = null,
                modifier = Modifier.size(128.dp),
                contentScale = ContentScale.Crop,
            )
            Text(text = "Current balance: ${info.etherBalance} ETH")
        }
    }
}
