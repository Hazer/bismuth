package com.vitusortner.patterns.networking

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

interface ApiClient {

    companion object {

        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl("https://api.pinterest.com/")
//                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        }

        val instance by lazy {
            retrofit.create(ApiClient::class.java)
        }
    }
}