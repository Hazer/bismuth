package com.vitusortner.patterns.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vitusortner.patterns.R
import com.vitusortner.patterns.networking.ApiClient
import com.vitusortner.patterns.observe
import com.vitusortner.patterns.service.AuthenticationService
import com.vitusortner.patterns.ui.pins.PinsAdapter
import com.vitusortner.patterns.ui.pins.PinsViewModel
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

    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var adapter: PinsAdapter

    private lateinit var viewModel: PinsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPrefs = SharedPrefs(this)
        authService = AuthenticationService(this, sharedPrefs)

        layoutManager = LinearLayoutManager(this)
        adapter = PinsAdapter()

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        viewModel = PinsViewModel.get(this, apiClient, sharedPrefs, ActualPatternsDispatchers)

        authService.onConnect()

        viewModel.pins.observe(this, adapter::submitList)
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
