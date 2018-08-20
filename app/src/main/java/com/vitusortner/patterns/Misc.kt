package com.vitusortner.patterns

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI
import retrofit2.Call
import retrofit2.Response

fun launchAsync(parent: Job? = null, block: suspend CoroutineScope.() -> Unit): Job =
    launch(UI, parent = parent) { block() }

suspend fun <T> Call<T>.await(): Response<T> =
    withContext(CommonPool) { execute() }

fun <T> LiveData<T>.observe(owner: LifecycleOwner, block: (T) -> Unit) =
    observe(owner, Observer { block(it) })

//

data class Token(val value: String)