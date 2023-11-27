package com.example.androidexamenproject.ui.viewModel

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.androidexamenproject.model.NFTCollection

@Composable
fun NftCollectionList(alchemyViewModel: AlchemyViewModel, modifier: Modifier = Modifier, navController: NavController){
    var nftCollectionList: List<NFTCollection> = alchemyViewModel.collectionsForOwner.value ?: emptyList()
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(minSize = 128.dp)
        ) {
        items(nftCollectionList) { nftCollection ->
            NFTCollectionCard(nftCollection = nftCollection, navController = navController)
        }
    }
}

@Composable
fun NFTCollectionCard(nftCollection: NFTCollection, modifier: Modifier = Modifier, navController: NavController){
    Card(modifier = Modifier
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

        }
        Text(text = nftCollection.name)
        AsyncImage(model = nftCollection.image?.pngUrl, contentDescription = nftCollection.description)
    }
}

