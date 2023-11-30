package com.example.androidexamenproject.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
    navController: NavHostController = rememberNavController(),
    alchemyViewModel: AlchemyViewModel = viewModel(factory = AlchemyViewModel.Factory)
) {
    var alchemyViewModel: AlchemyViewModel =
        viewModel(factory = AlchemyViewModel.Factory)
    Scaffold(
        bottomBar = {
            BottomAppBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
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
            }
        }
    }