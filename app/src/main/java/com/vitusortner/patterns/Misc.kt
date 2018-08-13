package com.vitusortner.patterns

import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI
import retrofit2.Call
import retrofit2.Response

fun launchAsync(block: suspend CoroutineScope.() -> Unit): Job = launch(UI) { block() }

suspend fun <T> Call<T>.await(): Response<T> = withContext(CommonPool) { execute() }

data class Token(val value: String)