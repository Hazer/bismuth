package com.vitusortner.patterns

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.vitusortner.patterns.networking.ApiClient
import com.vitusortner.patterns.service.AuthenticationService
import com.vitusortner.patterns.util.Logger
import com.vitusortner.patterns.util.SharedPrefs
import kotlinx.android.synthetic.main.activity_main.button

class MainActivity : AppCompatActivity() {

    private val log = Logger(javaClass)

    private val apiClient = ApiClient.instance

    private lateinit var sharedPrefs: SharedPrefs
    private lateinit var authService: AuthenticationService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPrefs = SharedPrefs(this)
        authService = AuthenticationService(this, sharedPrefs)

        authService.onConnect()

        button.setOnClickListener {
            authService.login()
        }
    }

//    private fun authenticate(code: String) {
//        launchAsync {
//            val response = apiClient.token(code).await()
//            log.i("Response: ${response.body()}")
//        }
//    }

}