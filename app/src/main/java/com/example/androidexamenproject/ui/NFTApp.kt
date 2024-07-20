package com.example.androidexamenproject.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.androidexamenproject.ui.viewModel.AlchemyViewModel
import com.example.androidexamenproject.ui.viewModel.EthNodeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NFTApp() {
    val navController: NavHostController = rememberNavController()

    val alchemyViewModel: AlchemyViewModel = viewModel(factory = AlchemyViewModel.Factory)
    val ethNodeViewModel: EthNodeViewModel = viewModel(factory = EthNodeViewModel.Factory)
    var navigateToCollections by remember { mutableStateOf(false) }

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    LaunchedEffect(key1 = Unit) {
        if (alchemyViewModel.ethereumAddress.value != "") {
            navController.navigate("collections")
        }
        alchemyViewModel.getEthereumAddress()
    }

    Scaffold(
        bottomBar = {
            if (currentRoute != "home") {
                BottomAppBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home"
        ) {
            composable("home") {
                Box(
                    modifier = Modifier.padding(innerPadding),
                ) {
                    GiveEthereumAddress(navController = navController, alchemyViewModel = alchemyViewModel)
                }
            }
            composable("collections") {
                Box(
                    modifier = Modifier.padding(innerPadding),
                ) {
                    NftCollectionList(alchemyViewModel = alchemyViewModel, navController = navController)
                }
            }
            composable("nfts") {
                Box(
                    modifier = Modifier.padding(innerPadding),
                ) {
                    NFTsPerCollectionList(alchemyViewModel = alchemyViewModel, navController = navController)
                }
            }
            composable("account") {
                Box(
                    modifier = Modifier.padding(innerPadding),
                ) {
                    AccountScreen(ethNodeViewModel = ethNodeViewModel)
                }
            }
        }
    }
}
