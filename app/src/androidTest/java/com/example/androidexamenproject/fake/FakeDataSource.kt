package com.example.androidexamenproject.fake

import com.example.androidexamenproject.model.DisplayNft
import com.example.androidexamenproject.model.Image
import com.example.androidexamenproject.model.NFTContract
import com.example.androidexamenproject.model.OpenSeaMetadata


object FakeDataSource {

    private val fakeNFTContracts: List<NFTContract> = listOf(
        NFTContract(
            address = "0x09f66a094a0070EBDdeFA192a33fa5d75b59D46b",
            name = "YAYO NFT",
            totalSupply = 10000,
            openSeaMetadata = OpenSeaMetadata(
                floorPrice = 0.151949,
                collectionName = "YAYO NFT",
                imageUrl = "https://openseauserdata.com/files/e369fa51ba28d5daecd837cb0cbfabfe.png",
                description = "YAYO NFT is a collection of 10,000 unique NFTs on the Ethereum blockchain...",
                externalUrl = "https://yayonft.com/",
                lastIngestedAt = "2021-10-13T15:00:00.000Z"
            ),
            totalBalance = "0",
            numDistinctTokensOwned = "27",
            displayNft = DisplayNft(
                tokenId = "0",
                name = "YAYO NFT #0"
            ),
            image = Image(
                cachedUrl = "https://openseauserdata.com/files/e369fa51ba28d5daecd837cb0cbfabfe.png",
                thumbnailUrl = "https://",
                pngUrl = "https://",
                originalUrl = "https://"
            )
        )
    )
    val fakeNfts = fakeNFTContracts

}
