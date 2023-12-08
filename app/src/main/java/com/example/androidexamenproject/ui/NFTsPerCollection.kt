package com.example.androidexamenproject.ui

import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.androidexamenproject.model.NftObject
import com.example.androidexamenproject.ui.viewModel.AlchemyViewModel

@Composable
fun NFTsPerCollectionList(
    navController: NavController,
    alchemyViewModel: AlchemyViewModel
){
    var gridColumns by remember { mutableStateOf(1) }
    val nfts = alchemyViewModel.nftsForOwner.value
    LaunchedEffect(nfts){
        alchemyViewModel.getNFTsForOwner(alchemyViewModel.ethereumAddress.value, listOf(alchemyViewModel.collectionContractAddress.value))
    }

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { gridColumns = if (gridColumns == 1) 2 else 1 }
        ) {
            Icon(
                Icons.Default.List,
                contentDescription = "change view",
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(gridColumns),
        ){
            items(nfts?.size ?: 0) { index ->
                NFTCard(nft = nfts?.get(index))
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NFTCard(
    nft: NftObject?
) {
    var showDetails by remember { mutableStateOf(false) }
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    var isImageClicked by remember { mutableStateOf(false) }

    if (isImageClicked) {
        Dialog(onDismissRequest = { isImageClicked = false }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                AsyncImage(
                    model = nft?.image?.cachedUrl ?: nft?.raw?.metadata?.image ?: "",
                    contentDescription = nft?.description ?: "",
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center),
                    imageLoader = imageLoader,
                )
            }
        }
    }
    ElevatedCard(
        onClick = { showDetails = !showDetails },
        modifier = Modifier.padding(16.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
                AsyncImage(
                    model = nft?.image?.cachedUrl ?: nft?.raw?.metadata?.image ?: "",
                    contentDescription = nft?.description ?: "",
                    modifier = Modifier
                        .size(300.dp)
                        .align(Alignment.Center)
                        .clickable { isImageClicked = true },
                    imageLoader = imageLoader,
                )
        }
        Column {
            Text(
                text = nft?.name ?: "No name",
                modifier = Modifier.padding(8.dp)
            )
            if (showDetails) {
                NFTDetails(nft = nft)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NFTDetails(
    nft: NftObject?
) {
    Column {
        Text("Description:")
        Text(text = nft?.description ?: "No description",
            modifier = Modifier.padding(8.dp))
    }
}
