package com.example.textrecognition.network

import com.example.textrecognition.network.entity.Receipt
import com.example.textrecognition.network.entity.TextInfoNetwork
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TextInfoService {
    @POST("Receip/Process")
    fun processTextInfo(@Body receipt: Receipt):Call<TextInfoNetwork>

    @GET("Receip/{clientId}")
    fun listReceipt(@Path("clientId") clientId: String): Call<List<TextInfoNetwork>>

    @GET("Receip/{clientId}/{id}")
    fun getReceipt(
        @Path("clientId") clientId: String,
        @Path("id") id: String
    ): Call<TextInfoNetwork>
}