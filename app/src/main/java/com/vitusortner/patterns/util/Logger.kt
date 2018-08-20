package com.vitusortner.patterns.util

import android.util.Log

class Logger<T>(javaClass: Class<T>) {

    private val tag = "${javaClass.simpleName}::class"

    fun i(message: String, throwable: Throwable? = null) = Log.i(tag, message, throwable)

    fun d(message: String, throwable: Throwable? = null) = Log.d(tag, message, throwable)

    fun e(message: String, throwable: Throwable? = null) = Log.e(tag, message, throwable)

    fun v(message: String, throwable: Throwable? = null) = Log.v(tag, message, throwable)

    fun w(message: String, throwable: Throwable? = null) = Log.w(tag, message, throwable)

}

fun <T : Any> T.logger() = lazy { Logger(javaClass) }