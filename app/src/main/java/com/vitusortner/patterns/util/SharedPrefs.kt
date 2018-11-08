package com.vitusortner.patterns.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.vitusortner.patterns.Token

/**
 * SharedPreferences wrapper to easily save and retrieve data from SharedPreferences.
 * Shouldn't be instanciated more often than once
 */
class SharedPrefs(context: Context) {

    private val PREFS_TOKEN = "prefs_token"

    private val preferences = (context as Activity).getPreferences(Context.MODE_PRIVATE)

    /**
     * Holds Pinterest access token.
     */
    var token: Token?
        set(token) {
            preferences.edit { putString(PREFS_TOKEN, token?.value) }
        }
        get() {
            val token = preferences.getString(PREFS_TOKEN, null) ?: return null
            return Token(token)
        }

    //

    /**
     * Helper function to store data in SharedPreferences.
     */
    private fun SharedPreferences.edit(action: SharedPreferences.Editor.() -> Unit) {
        edit().apply {
            action()
            apply()
        }
    }
}
