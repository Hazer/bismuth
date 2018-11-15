package com.vitusortner.patterns

import com.vitusortner.patterns.util.PatternsDispatchers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
object TestDispatchers : PatternsDispatchers {
    override val IO = Dispatchers.Unconfined
    override val Main = Dispatchers.Unconfined
}
