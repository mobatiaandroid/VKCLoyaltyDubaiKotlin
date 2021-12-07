package com.vkc.loyaltyme.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private lateinit var apiService: ApiService
    var BASE_URL = "http://dev.mobatia.com/vkc2.5/apiv2/" // Dev
//    var BASE_URL = "https://mobile.walkaroo.in/vkc/" // Live
//    var BASE_URL = "http://54.84.5.69/vkc/" // Live

    fun getApiService(): ApiService {
        val client = OkHttpClient.Builder().build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        apiService = retrofit.create(ApiService::class.java)
        return apiService
    }
}