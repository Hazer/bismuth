package com.vitusortner.patterns.networking.model

import com.squareup.moshi.Json

data class ServerResponse<T>(val data: T)

data class TokenResponse(
    @field:Json(name = "access_token")
    val token: String,

    @field:Json(name = "token_type")
    val type: String,

    val scope: List<String>
)