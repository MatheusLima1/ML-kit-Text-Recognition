package com.example.textrecognition.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitInicializr {

    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(5,TimeUnit.SECONDS)
        .build()

    private val retrofit =     Retrofit.Builder()
            .baseUrl("https://corporaterefund.azurewebsites.net")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    fun textInfoService():TextInfoService{
        return retrofit.create(TextInfoService::class.java)
    }
}