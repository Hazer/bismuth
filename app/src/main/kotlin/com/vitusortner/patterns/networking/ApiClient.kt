package com.vitusortner.patterns.networking

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.serjltt.moshi.adapters.Wrapped
import com.squareup.moshi.Moshi
import com.vitusortner.patterns.networking.model.I
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiClient {

    @GET("me/pins/?fields=image")
    @Wrapped(path = ["data"])
    fun images(@Query("access_token") token: String): Deferred<List<I>>

    companion object {

        val instance: ApiClient by lazy {
            retrofit.create(ApiClient::class.java)
        }

        private val moshi by lazy {
            Moshi.Builder()
                .add(Wrapped.ADAPTER_FACTORY)
                .build()
        }

        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl("https://api.pinterest.com/v1/")
//                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
        }
    }
}
