package com.example.androidexamenproject.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.androidexamenproject.model.NFTCollection
import com.example.androidexamenproject.ui.viewModel.AlchemyViewModel

@Composable
fun NftCollectionList(alchemyViewModel: AlchemyViewModel, modifier: Modifier = Modifier, navController: NavController){
    var nftCollectionList: List<NFTCollection> = alchemyViewModel.collectionsForOwner.value ?: emptyList()

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(minSize = 128.dp)
        ) {
        items(nftCollectionList) { nftCollection ->

            NFTCollectionCard(
                nftCollection = nftCollection,
                navController = navController,
                viewModel = alchemyViewModel,
            )
        }
    }
}

@Composable
fun NFTCollectionCard(
    nftCollection: NFTCollection,
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: AlchemyViewModel
){
    var imageUrls by rememberSaveable { mutableStateOf(mapOf<String?, String?>()) }

    LaunchedEffect(nftCollection.contract?.address) {
        viewModel.getContractMetadata(nftCollection.contract?.address ?: "") { metaData ->
            metaData?.openSeaMetadata?.imageUrl?.let { imageUrl ->
                // Update the map with the new image URL
                imageUrls = (imageUrls + (nftCollection.contract?.address to imageUrl)).toMap()
            }
        }
    }



    val imageUrl = imageUrls[nftCollection.contract?.address] ?: ""

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { },
        shape = RoundedCornerShape(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = nftCollection.name)
            AsyncImage(
                model = imageUrl,
                contentDescription = nftCollection.description
            )
        }
    }
}


