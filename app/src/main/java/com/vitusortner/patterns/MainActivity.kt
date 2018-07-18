package com.vitusortner.patterns

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val log = Logger(javaClass)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val action = intent.action
        val data = intent.data

        log.i("Action: $action, data: $data")
    }
}
