package com.example.androidexamenproject.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "ethereumAddresses")
open class EthereumAddress (
    @PrimaryKey
    val ethAddress: String,
    val ensAddress: String? = null
)
