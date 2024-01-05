package com.example.androidexamenproject.ui

import android.util.Log
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
import androidx.navigation.compose.rememberNavController
import com.example.androidexamenproject.ui.viewModel.AlchemyViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NFTApp(
) {
    var navController: NavHostController = rememberNavController()

    var alchemyViewModel: AlchemyViewModel =
        viewModel(factory = AlchemyViewModel.Factory)
    var navigateToCollections by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        Log.d("ethaddress", alchemyViewModel.ethereumAddress.value)
        if (alchemyViewModel.ethereumAddress.value != "") {
            navController.navigate("collections")
        }
        alchemyViewModel.getEthereumAddress()
    }
    Scaffold(
        bottomBar = {
            BottomAppBar(navController = navController)
        }
    ) { innerPadding ->
        //Log.d("ethaddress", alchemyViewModel.ethereumAddress.collectAsState().value)
        NavHost(
            navController = navController,
            startDestination = "home"

            ) {
            composable("home") {
                Box(
                    modifier = Modifier
                        .padding(innerPadding),
                ) {
                    GiveEthereumAddress(navController = navController, alchemyViewModel = alchemyViewModel)
                }
            }
            composable("collections") {
                Box(
                    modifier = Modifier
                        .padding(innerPadding),
                ) {
                        NftCollectionList(alchemyViewModel = alchemyViewModel, navController = navController)
                    }
                }
            composable("nfts") {
                Box(
                    modifier = Modifier
                        .padding(innerPadding),
                ) {
                    NFTsPerCollectionList(
                        alchemyViewModel = alchemyViewModel,
                        navController = navController
                    )}
                }
            }
        }
    }