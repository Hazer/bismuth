package com.vitusortner.patterns.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class SharedPrefs(context: Context) {

    private val PREFS_TOKEN = "prefs_token"

    private val preferences = (context as Activity).getPreferences(Context.MODE_PRIVATE)

    var token: String?
        set(value) {
            preferences.edit { putString(PREFS_TOKEN, value) }
        }
        get() {
            return preferences.getString(PREFS_TOKEN, null)
        }

    //

    private fun SharedPreferences.edit(action: SharedPreferences.Editor.() -> Unit) {
        edit().apply {
            action()
            apply()
        }
    }
}