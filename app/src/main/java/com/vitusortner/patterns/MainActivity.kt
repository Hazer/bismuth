package com.vitusortner.patterns

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vitusortner.patterns.networking.ApiClient
import com.vitusortner.patterns.service.AuthenticationService
import com.vitusortner.patterns.util.Logger
import com.vitusortner.patterns.util.SharedPrefs
import kotlinx.android.synthetic.main.activity_main.recyclerView

class MainActivity : AppCompatActivity() {

    private val log = Logger(javaClass)

    private val apiClient = ApiClient.instance

    // probably move these to a service locator or Koin
    private lateinit var sharedPrefs: SharedPrefs
    private lateinit var authService: AuthenticationService

    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var adapter: PinsAdapter

    //

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPrefs = SharedPrefs(this)
        authService = AuthenticationService(this, sharedPrefs)

        layoutManager = LinearLayoutManager(this)
        adapter = PinsAdapter()

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

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
            val response =
                apiClient.images(token.value).await().apply { log.i("Response: ${body()}") }

            val images = response.body() ?: return@launchAsync

            adapter.items = images
        }
    }

}