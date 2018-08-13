package com.vitusortner.patterns.networking.model

// TODO this is ugly
data class I(val image: O)
data class O(val original: Image)

data class Image(
    val url: String,
    val width: Int,
    val height: Int
)