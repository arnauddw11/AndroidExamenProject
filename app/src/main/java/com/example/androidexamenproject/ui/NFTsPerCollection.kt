package com.example.androidexamenproject.ui

import android.util.Log
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.androidexamenproject.model.Contract
import com.example.androidexamenproject.model.NftObject
import com.example.androidexamenproject.ui.viewModel.AlchemyViewModel

@Composable
fun NFTsPerCollectionList(
    navController: NavController,
    alchemyViewModel: AlchemyViewModel
){
    val nfts = alchemyViewModel.nftsForOwner.value
    LaunchedEffect(nfts){
        alchemyViewModel.getNFTsForOwner(alchemyViewModel.ethereumAddress.value, listOf(alchemyViewModel.collectionContractAddress.value))
        Log.d("NFTsPerCollectionList", "NFTsPerCollectionList: ${alchemyViewModel.nftsForOwner.value}")
    }
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp)
    ){
    items(nfts?.size ?: 0){ index ->
        NFTCard(nft = nfts?.get(index))
    }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NFTCard(
    nft: NftObject?
){
    Log.d("tokenUri", "NFTCard: ${nft?.tokenUri}")
    ElevatedCard(onClick = { /*TODO*/ }) {
        AsyncImage(
            model = nft?.tokenUri ?: "",
            contentDescription = nft?.description ?: "",
            modifier = Modifier
                .size(60.dp)
                .clip(
                    RoundedCornerShape(4.dp)
                )
        )
        Text(text = nft?.name ?: "No name")
    }
}