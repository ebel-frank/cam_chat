package com.horizons.camchat.ui.login

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: Boolean? = false,
    val error: String? = null
)