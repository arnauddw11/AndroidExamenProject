package com.example.androidexamenproject.ui

import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.androidexamenproject.model.NftObject
import com.example.androidexamenproject.model.Rarity
import com.example.androidexamenproject.ui.viewModel.AlchemyViewModel


@Composable
fun NFTsPerCollectionList(
    navController: NavController,
    alchemyViewModel: AlchemyViewModel
) {
    var gridColumns by remember { mutableStateOf(1) }
    val nfts = alchemyViewModel.nftsForOwner.value

    LaunchedEffect(nfts) {
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
                modifier = Modifier.align(Alignment.Center)
            )
        }



        LazyVerticalGrid(
            columns = GridCells.Fixed(gridColumns),
        ) {
            item {
                NameAndDescription(nfts = nfts)
            }
            items(nfts?.size ?: 0) { index ->
                NFTCard(nft = nfts?.get(index), alchemyViewModel = alchemyViewModel)
            }
        }
    }
}

@Composable
fun NameAndDescription(
    nfts: List<NftObject>?
) {
    nfts?.firstOrNull()?.let { firstNft ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Text(
                    text = firstNft.collection?.name ?: "No name available",
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 30.sp,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 3.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 30.dp, end = 30.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Floor price",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Text(
                        text = firstNft?.contract?.openSeaMetadata?.floorPrice?.toString()?.let { "  $it ETH" } ?: "No floor price available",
                        modifier = Modifier.padding(top = 8.dp, bottom = 10.dp)
                    )
                }
                Text(
                    text = firstNft.description ?: "No description available"
                )

            }

        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NFTCard(
    nft: NftObject?,
    alchemyViewModel: AlchemyViewModel
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
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = nft?.image?.cachedUrl ?: nft?.raw?.metadata?.image ?: "",
                contentDescription = nft?.description ?: "",
                modifier = Modifier
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
                NFTDetails(nft = nft, alchemyViewModel = alchemyViewModel)
            }
        }
    }
}


@Composable
fun NFTDetails(
    nft: NftObject?,
    alchemyViewModel: AlchemyViewModel
) {
    val rarities = alchemyViewModel.rarities.collectAsState().value

    LaunchedEffect(rarities) {
        alchemyViewModel.computeRarity(nft?.contract?.address.toString(), nft?.tokenId.toString())
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Rarity:",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp)
        )
        if (rarities != null) {
            Column {
                rarities.forEach { rarity ->
                    RarityBadge(rarity = rarity)
                }
            }
        }
    }
}

@Composable
fun RarityBadge(rarity: Rarity) {
    Surface(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Text(
                text = rarity.traitType,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = rarity.value + " - " + (rarity.prevalence * 100).format(2) + "%",
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

// Extension function to format double to 2 decimal places
fun Double.format(digits: Int) = "%.${digits}f".format(this)
