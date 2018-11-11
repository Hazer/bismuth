package com.vitusortner.patterns.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.vitusortner.patterns.R
import com.vitusortner.patterns.networking.ApiClient
import com.vitusortner.patterns.networking.model.Image
import com.vitusortner.patterns.observe
import com.vitusortner.patterns.service.AuthenticationService
import com.vitusortner.patterns.ui.pins.PinsViewModel
import com.vitusortner.patterns.ui.pins._PinsAdaper
import com.vitusortner.patterns.util.ActualPatternsDispatchers
import com.vitusortner.patterns.util.Logger
import com.vitusortner.patterns.util.SharedPrefs
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val log by Logger()

    private val apiClient = ApiClient.instance

    // probably move these to a service locator or Koin
    private lateinit var sharedPrefs: SharedPrefs
    private lateinit var authService: AuthenticationService

    //    private lateinit var adapter: PinsAdapter
//    private lateinit var adapter: _PinsAdaper

    private lateinit var viewModel: PinsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPrefs = SharedPrefs(this)
        authService = AuthenticationService(this, sharedPrefs)

        //        adapter = PinsAdapter()
        val adapter = _PinsAdaper()

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.setHasFixedSize(true)
        recyclerView.setItemViewCacheSize(20)
        recyclerView.isNestedScrollingEnabled = false

        viewModel = PinsViewModel(this, apiClient, sharedPrefs, ActualPatternsDispatchers)

        authService.onConnect()

        val images = listOf(
            "https://images.pexels.com/photos/305821/pexels-photo-305821.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
            "https://images.pexels.com/photos/1576002/pexels-photo-1576002.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
            "https://images.pexels.com/photos/1580470/pexels-photo-1580470.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260"
        ).map { Image(url = it, width = 0, height = 0) }

        val items = (0..30).map { images.random() }

        adapter.items = items

//        viewModel.pins.observe(this, adapter::submitList)
        viewModel.pins.observe(this) {
            log.d("Items: $it")
            adapter.items = it
        }
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
                viewModel.fetchPins()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

}
