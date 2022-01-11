package com.example.textrecognition.network.entity

import com.google.gson.annotations.SerializedName

data class Receipt(
    @SerializedName("textReceip" ) var textReceip : String? = null,
    @SerializedName("clientId"   ) var clientId   : String? = null
)
