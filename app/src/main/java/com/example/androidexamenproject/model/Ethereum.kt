package com.example.androidexamenproject.model

import kotlinx.serialization.Serializable
/**
 * Represents user information related to an Ethereum account, including ENS address, resolved Ethereum address,
 * balance in Ether, and an avatar image URL.
 *
 * @property ensAddress The ENS (Ethereum Name Service) address associated with the Ethereum account, if available.
 * @property resolvedAddress The resolved Ethereum address, which may be derived from the ENS address or directly provided.
 * @property etherBalance The balance of the Ethereum account, measured in Ether (ETH).
 * @property avatar The URL of the avatar image associated with the user's ENS address, if available.
 */
@Serializable
data class UserInfo(
    val ensAddress: String?,
    val resolvedAddress: String?,
    val etherBalance: Double?,
    val avatar: String?
)