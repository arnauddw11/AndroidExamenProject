package com.example.androidexamenproject.model

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val ensAddress: String?,
    val resolvedAddress: String?,
    val etherBalance: Double?,
    val avatar: String?
)