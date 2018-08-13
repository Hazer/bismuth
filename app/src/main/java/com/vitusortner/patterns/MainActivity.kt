package com.vitusortner.patterns

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.vitusortner.patterns.networking.ApiClient
import com.vitusortner.patterns.service.AuthenticationService
import com.vitusortner.patterns.util.Logger
import com.vitusortner.patterns.util.SharedPrefs

class MainActivity : AppCompatActivity() {

    private val log = Logger(javaClass)

    private val apiClient = ApiClient.instance

    // probably move these to a service locator or Koin
    private lateinit var sharedPrefs: SharedPrefs
    private lateinit var authService: AuthenticationService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPrefs = SharedPrefs(this)
        authService = AuthenticationService(this, sharedPrefs)

        authService.onConnect()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.settings_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.login -> {
                authService.login()
                true
            }
            R.id.refresh -> {
                refresh()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun refresh() {
        val token = sharedPrefs.token ?: run {
            log.i("Not logged in!")
            return
        }

        launchAsync {
            val images = apiClient.images(token.value).await()
            log.i("Fetched images: ${images.body()}")
        }
    }

}