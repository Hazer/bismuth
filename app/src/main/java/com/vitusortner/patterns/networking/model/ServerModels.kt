package com.vitusortner.patterns.networking.model

data class I(val image: O)
data class O(val original: Image)

data class Image(
    val url: String,
    val width: Int,
    val height: Int
)