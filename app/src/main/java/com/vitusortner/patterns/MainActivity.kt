package com.vitusortner.patterns

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.vitusortner.patterns.networking.ApiClient
import com.vitusortner.patterns.util.Logger
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val log = Logger(javaClass)

    private val apiClient = ApiClient.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        handleIntent(intent)

        button.setOnClickListener { openPinterestAuthorization() }
    }

    private fun handleIntent(intent: Intent) {
        log.i("Intent: $intent")

        intent.data?.getQueryParameter("code")?.also { log.i("Code: $it") }?.let(::authenticate)
    }

    private fun openPinterestAuthorization() {
        val uri = Uri.parse(Constants.AUTHORIZATION_URL)
        val intent = Intent(Intent.ACTION_VIEW, uri)

        intent.resolveActivity(packageManager)?.let {
            startActivity(intent)
        }
    }

    private fun authenticate(code: String) {
        launchAsync {
            val response = apiClient.token(code).await()
            log.i("Response: ${response.body()}")
        }
    }

}