package com.example.androidexamenproject.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.androidexamenproject.R
import com.example.androidexamenproject.model.NFTCollection
import com.example.androidexamenproject.ui.viewModel.AlchemyViewModel

@Composable
fun NftCollectionList(alchemyViewModel: AlchemyViewModel, modifier: Modifier = Modifier, navController: NavController){
    var nftCollectionList: List<NFTCollection> = alchemyViewModel.collectionsForOwner.value ?: emptyList()
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
        ) {
        item {
            Text(
                text = "NFT Collections",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                color = MaterialTheme.colorScheme.primary,
            )
        }
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
    val floorPrice = nftCollection.floorPrice?.floorPrice ?: 0.0
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { },
        shape = RoundedCornerShape(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(text = nftCollection.name)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Floor Price: ${floorPrice}")
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
                model = imageUrl,
                contentDescription = nftCollection.description,
                modifier = Modifier
                    .size(60.dp)
                    .clip(
                        RoundedCornerShape(4.dp)
                    )
            )
        }
    }
}




