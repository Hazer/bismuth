package com.vitusortner.patterns.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface PatternsDispatchers {
    val IO: CoroutineDispatcher
    val Main: CoroutineDispatcher
}

object ActualPatternsDispatchers : PatternsDispatchers {
    override val IO = Dispatchers.IO
    override val Main = Dispatchers.Main
}
