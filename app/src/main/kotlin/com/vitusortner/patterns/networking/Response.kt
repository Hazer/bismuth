package com.vitusortner.patterns.networking

@Suppress("unused")
sealed class Response<out T : Any> {
    object Loading : Response<Nothing>()
    data class Success<out T : Any>(val data: T) : Response<T>()
    data class Error(val throwable: Throwable) : Response<Nothing>()
}
