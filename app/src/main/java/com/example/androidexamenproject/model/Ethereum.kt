package com.example.androidexamenproject.model

import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class UserInfo(
    val EnsAddress: String?,
    val resolvedAddress: String?,
    val EthEtherBalance: BigDecimal?,
    val avatar: String?
)