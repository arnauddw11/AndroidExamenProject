package com.example.androidexamenproject.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "ethereumAddresses")
data class EthereumAddress (
    @PrimaryKey
    val ethAddress: String
)
