package com.vitusortner.patterns.util

import android.util.Log

class Logger<T>(javaClass: Class<T>) {

    private val tag = "${javaClass.simpleName}::class"

    fun i(message: String) = Log.i(tag, message)

    fun d(message: String) = Log.d(tag, message)

    fun e(message: String) = Log.e(tag, message)

    fun v(message: String) = Log.v(tag, message)

    fun w(message: String) = Log.w(tag, message)
}