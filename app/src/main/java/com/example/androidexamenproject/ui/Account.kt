package com.example.androidexamenproject.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.androidexamenproject.ui.viewModel.EthNodeViewModel

@Composable
fun AccountScreen(
    ethNodeViewModel: EthNodeViewModel
) {
    val userInfo by ethNodeViewModel.userInfo.collectAsState()
    val hasFetchedUserInfo = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!hasFetchedUserInfo.value) {
            ethNodeViewModel.getUserInfo()
            hasFetchedUserInfo.value = true
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Profile", style = MaterialTheme.typography.headlineLarge)
        userInfo?.let { info ->

            Text(
                text = info.ensAddress.toString(),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom =  8.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = info.resolvedAddress.toString(),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(bottom= 16.dp),
                textAlign = TextAlign.Center
            )
            AsyncImage(
                model = info.avatar.toString(),
                contentDescription = "profileImage",
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 16.dp),
                contentScale = ContentScale.Crop,
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Balance: ",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "${info.etherBalance} ETH",
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary)
                )
            }
        }
    }
}