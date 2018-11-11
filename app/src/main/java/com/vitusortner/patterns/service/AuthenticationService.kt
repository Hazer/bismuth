package com.vitusortner.patterns.service

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.vitusortner.patterns.Constants
import com.vitusortner.patterns.Token
import com.vitusortner.patterns.util.Logger
import com.vitusortner.patterns.util.SharedPrefs

class AuthenticationService(private val context: Context, private val sharedPrefs: SharedPrefs) {

    private val log by Logger()

    @Suppress("PrivatePropertyName")
    private val URL =
        "https://api.pinterest.com/oauth/?" +
                "response_type=token&" +
                "redirect_uri=pdk${Constants.APP_ID}://&" +
                "client_id=${Constants.APP_ID}&" +
                "scope=read_public,write_public"

    /**
     * Call function to initiate login.
     */
    fun login() {
        initiateWebLogin()
    }

    /**
     * Call function to retrieve intent data.
     */
    fun onConnect() {
        val result = (context as Activity).intent.data?.toString() ?: return

        if (result.contains("pdk${Constants.APP_ID}://")) {
            onOauthResponse(result)
        }
    }

    private fun initiateWebLogin() {
        val uri = Uri.parse(URL)
        val intent = Intent(Intent.ACTION_VIEW, uri)

        intent.resolveActivity(context.packageManager)?.let {
            context.startActivity(intent)
        }
    }

    private fun onOauthResponse(result: String) {
        val token = Uri.parse(result).getQueryParameter("access_token") ?: return
        log.d("Received token: $token")

        sharedPrefs.token = Token(token)
    }

}
