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
    // Initialize the navigation controller
    val navController: NavHostController = rememberNavController()

    // Initialize the view models for the app
    val alchemyViewModel: AlchemyViewModel = viewModel(factory = AlchemyViewModel.Factory)
    val ethNodeViewModel: EthNodeViewModel = viewModel(factory = EthNodeViewModel.Factory)
    var navigateToCollections by remember { mutableStateOf(false) }

    // Get the current back stack entry to determine the current route
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    // Launch a side effect to navigate to the collections screen if an Ethereum address is already set
    LaunchedEffect(key1 = Unit) {
        if (alchemyViewModel.ethereumAddress.value != "") {
            navController.navigate("collections")
        }
        alchemyViewModel.getEthereumAddress()
    }

    // Main app scaffold with a conditional bottom bar
    Scaffold(
        bottomBar = {
            if (currentRoute != "home") {
                BottomAppBar(navController = navController, alchemyViewModel = alchemyViewModel)
            }
        }
    ) { innerPadding ->
        // Navigation host to manage different screens in the app
        NavHost(
            navController = navController,
            startDestination = "home"
        ) {
            // Home screen where the user can input their Ethereum address or ENS name
            composable("home") {
                Box(
                    modifier = Modifier.padding(innerPadding),
                ) {
                    GiveEthereumAddress(navController = navController, alchemyViewModel = alchemyViewModel)
                }
            }
            // Screen that displays a list of NFT collections owned by the user
            composable("collections") {
                Box(
                    modifier = Modifier.padding(innerPadding),
                ) {
                    NftCollectionList(alchemyViewModel = alchemyViewModel, navController = navController)
                }
            }
            // Screen that displays NFTs from a selected collection
            composable("nfts") {
                Box(
                    modifier = Modifier.padding(innerPadding),
                ) {
                    NFTsPerCollectionList(alchemyViewModel = alchemyViewModel, navController = navController)
                }
            }
            // Screen that displays the user's account details
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
