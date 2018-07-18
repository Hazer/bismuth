package com.vitusortner.patterns

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface ApiClient {

    @GET("oauth/")
    fun authorizationCode(): Call<String>

    companion object {

        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl("https://api.pinterest.com/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        }

        val instance by lazy {
            retrofit.create(ApiClient::class.java)
        }
    }
}