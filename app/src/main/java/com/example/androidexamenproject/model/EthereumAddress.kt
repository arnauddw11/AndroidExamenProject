package com.example.androidexamenproject.model

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Represents an Ethereum address entity that may include an ENS (Ethereum Name Service) address and an avatar URL.
 *
 * @property ethAddress The Ethereum address (public key) that uniquely identifies the account on the blockchain.
 * @property ensAddress The optional ENS (Ethereum Name Service) address associated with the Ethereum address.
 * @property avatar The optional URL of the avatar image associated with the ENS address.
 */
@Entity(tableName = "ethereumAddresses")
data class EthereumAddress (
    @PrimaryKey
    val ethAddress: String,
    val ensAddress: String? = null,
    val avatar: String? = null
)
