package com.vitusortner.patterns

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.vitusortner.patterns.networking.ApiClient
import com.vitusortner.patterns.service.AuthenticationService
import com.vitusortner.patterns.util.Logger
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val log = Logger(javaClass)

    private val apiClient = ApiClient.instance

    private lateinit var authService: AuthenticationService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        authService = AuthenticationService()

        authService.onConnect(this)

        button.setOnClickListener {
            authService.login(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        log.d("onActivityResult($requestCode, $resultCode, $data)")
        super.onActivityResult(requestCode, resultCode, data)

        data ?: return
        authService.onOauthResponse(requestCode, resultCode, data)
    }

//    private fun authenticate(code: String) {
//        launchAsync {
//            val response = apiClient.token(code).await()
//            log.i("Response: ${response.body()}")
//        }
//    }

}