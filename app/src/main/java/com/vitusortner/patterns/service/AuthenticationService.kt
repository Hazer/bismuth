package com.vitusortner.patterns.service

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import com.vitusortner.patterns.Constants
import com.vitusortner.patterns.util.Logger

class AuthenticationService {

    private val log = Logger(javaClass)

    private val PINTEREST_PACKAGE = "com.pinterest"
    private val PINTEREST_OAUTH_ACTIVITY = "com.pinterest.sdk.PinterestOauthActivity"

    private val EXTRA_APPID = "PDKCLIENT_EXTRA_APPID"
    private val EXTRA_APPNAME = "PDKCLIENT_EXTRA_APPNAME"
    private val EXTRA_PERMISSIONS = "PDKCLIENT_EXTRA_PERMISSIONS"
    private val EXTRA_RESULT = "PDKCLIENT_EXTRA_RESULT"

    private val REQUEST_CODE = 8772

    private val PREFS_TOKEN = "prefs_token"

    private val PERMISSION_READ_PUBLIC = "read_public"
    private val PERMISSION_WRITE_PUBLIC = "write_public"
    private val PERMISSION_READ_PRIVATE = "read_private"
    private val PERMISSION_WRITE_PRIVATE = "write_private"
    private val PERMISSION_READ_RELATIONSHIPS = "read_relationships"
    private val PERMISSION_WRITE_RELATIONSHIPS = "write_relationships"

    fun login(context: Context) {
//        if (pinterestInstalled(context)) {
//            initiateLogin(context)
//        } else {
//            initiateWebLogin(context)
//        }
        initiateWebLogin(context)
    }

    private fun initiateLogin(context: Context) {
        val permissions = listOf(PERMISSION_READ_PUBLIC, PERMISSION_WRITE_PUBLIC)
        val intent = authIntent(permissions)

        openPinterestAppForLogin(context, intent)
    }

    private fun openPinterestAppForLogin(context: Context, intent: Intent) {
        try {
            (context as Activity).startActivityForResult(intent, REQUEST_CODE)
        } catch (exception: ActivityNotFoundException) {

        }
    }

    private val url =
        "https://api.pinterest.com/oauth/?" +
                "response_type=token&" +
                "redirect_uri=pdk${Constants.APP_ID}://&" +
                "client_id=${Constants.APP_ID}&" +
                "scope=read_public,write_public"

    private fun initiateWebLogin(context: Context) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)

        intent.resolveActivity(context.packageManager)?.let {
            context.startActivity(intent)
        }
    }

    fun onConnect(context: Context) {
        val result = (context as Activity).intent.data?.toString() ?: return

        if (result.contains("pdk${Constants.APP_ID}://")) {
            onOauthResponse(result)
        }
    }

    fun onOauthResponse(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val result = data.getStringExtra(EXTRA_RESULT)
                log.i("Extra: $result")
                result?.let(::onOauthResponse)
            }
        }
    }

    private fun onOauthResponse(result: String) {
        val token = Uri.parse(result).getQueryParameter("access_token")
        log.i("Token: $token")
//        save()
    }

    private fun authIntent(permissions: List<String>): Intent {
        return Intent()
            .setClassName(PINTEREST_PACKAGE, PINTEREST_OAUTH_ACTIVITY)
            .putExtra(EXTRA_APPID, Constants.APP_ID)
            .putExtra(EXTRA_APPNAME, "appName")
            .putExtra(EXTRA_PERMISSIONS, permissions.joinToString(","))
    }

    private fun pinterestInstalled(context: Context): Boolean {
        val info = context.packageManager.getPackageInfo(PINTEREST_PACKAGE, 0)
        return info.versionCode >= 16 // Pinterest verion must be higher than 16
    }

    private fun save(context: Context, token: String) {
        val prefs = (context as Activity).getPreferences(Context.MODE_PRIVATE)
        prefs.edit { putString(PREFS_TOKEN, token) }
    }

    private fun SharedPreferences.edit(action: SharedPreferences.Editor.() -> Unit) {
        edit().apply {
            action()
            apply()
        }
    }
}