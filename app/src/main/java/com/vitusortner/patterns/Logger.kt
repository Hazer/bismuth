package com.vitusortner.patterns

import android.util.Log

class Logger<T>(javaClass: Class<T>) {

    val tag = "${javaClass.simpleName}::class"

    fun i(message: String) = Log.i(tag, message)
}