package com.example.androidexamenproject.ui

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.androidexamenproject.R
import com.example.androidexamenproject.model.NFTContract
import com.example.androidexamenproject.ui.viewModel.AlchemyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NftCollectionList(alchemyViewModel: AlchemyViewModel, modifier: Modifier = Modifier, navController: NavController) {

    var nftCollectionList: List<NFTContract> = alchemyViewModel.contractsForOwner.collectAsState().value ?: listOf()
    var filteredCollectionList by remember { mutableStateOf(nftCollectionList) }
    var filterApplied by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    var sortByHighestFloorPrice by remember { mutableStateOf(false) }
    var sortByLowestFloorPrice by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = alchemyViewModel.ethereumAddress.collectAsState().value,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = "NFT Collections",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
        )
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    TextField(
                        value = searchText,
                        onValueChange = {
                            searchText = it
                            filteredCollectionList = filterAndSortCollections(
                                nftCollectionList,
                                it,
                                sortByHighestFloorPrice,
                                sortByLowestFloorPrice
                            )
                            filterApplied =
                                it.isNotEmpty() || sortByHighestFloorPrice || sortByLowestFloorPrice
                        },
                        label = { Text("Search Collection") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        singleLine = true,
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(text = "Sort by:")
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(onClick = {
                                sortByHighestFloorPrice = !sortByHighestFloorPrice
                                sortByLowestFloorPrice = false
                                filteredCollectionList = filterAndSortCollections(
                                    nftCollectionList,
                                    searchText,
                                    sortByHighestFloorPrice,
                                    sortByLowestFloorPrice
                                )
                                filterApplied = searchText.isNotEmpty() || sortByHighestFloorPrice
                            }) {
                                Text("Highest floor price")
                            }
                            Button(onClick = {
                                sortByLowestFloorPrice = !sortByLowestFloorPrice
                                sortByHighestFloorPrice = false
                                filteredCollectionList = filterAndSortCollections(
                                    nftCollectionList,
                                    searchText,
                                    sortByHighestFloorPrice,
                                    sortByLowestFloorPrice
                                )
                                filterApplied = searchText.isNotEmpty() || sortByLowestFloorPrice
                            }) {
                                Text("Lowest floor price")
                            }
                        }
                    }
                }
            }
            items(if (filterApplied) filteredCollectionList else nftCollectionList) { nftCollection ->
                NFTCollectionCard(
                    nftCollection = nftCollection,
                    navController = navController,
                    viewModel = alchemyViewModel,
                )
            }
        }
    }
}

fun filterAndSortCollections(collections: List<NFTContract>, query: String, sortByHighestFloorPrice: Boolean, sortByLowestFloorPrice: Boolean): List<NFTContract> {
    val filteredList = collections.filter { collection ->
        collection.name?.lowercase()?.contains(query.lowercase()) == true
    }
    return when {
        sortByHighestFloorPrice -> filteredList.sortedByDescending { it.openSeaMetadata?.floorPrice }
        sortByLowestFloorPrice -> filteredList.sortedBy { it.openSeaMetadata?.floorPrice }
        else -> filteredList
    }
}




@Composable
fun NFTCollectionCard(
    nftCollection: NFTContract,
    navController: NavController,
    viewModel: AlchemyViewModel
){
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                viewModel.setCollectionContractAddress(nftCollection?.address ?: "")
                navController.navigate("nfts")
            },
        shape = RoundedCornerShape(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = nftCollection?.name ?: "Missing collection name")
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Floor Price: ${nftCollection?.openSeaMetadata?.floorPrice?.toString() ?: ""}")
                    Spacer(modifier = Modifier.width(4.dp))
                    Image(
                        painter = painterResource(id = R.drawable.ethlogo),
                        contentDescription = "ETH Logo",
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(15.dp)
                    )
                }
                Text(text = "Amount owned: " + nftCollection?.numDistinctTokensOwned.toString())
            }
            AsyncImage(
                model = nftCollection?.openSeaMetadata?.imageUrl ?: "",
                contentDescription = nftCollection?.openSeaMetadata?.description ?: "",
                modifier = Modifier
                    .size(60.dp)
                    .clip(
                        RoundedCornerShape(4.dp)
                    ),
                imageLoader = imageLoader
            )
        }
    }
}




