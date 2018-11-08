package com.vitusortner.patterns

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * Convenience function to observe changes of the [LiveData] without the need to create
 * an [Observer] each time by hand.
 */
fun <T> LiveData<T>.observe(owner: LifecycleOwner, block: (T) -> Unit) =
    observe(owner, Observer { block(it) })

data class Token(val value: String)
