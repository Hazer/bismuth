package com.vitusortner.patterns.networking

import com.vitusortner.patterns.Constants
import com.vitusortner.patterns.Secrets
import com.vitusortner.patterns.networking.model.TokenResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiClient {

    @POST(
        "v1/oauth/token?" +
                "grant_type=authorization_code&" +
                "client_id=${Constants.APP_ID}&" +
                "client_secret=${Secrets.APP_SECRET}"
    )
    fun token(@Query("code") code: String): Call<TokenResponse>

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