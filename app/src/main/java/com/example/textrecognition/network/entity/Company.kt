package com.example.textrecognition.network.entity

import com.google.gson.annotations.SerializedName

data class Company(
    @SerializedName("document") var document: String? = null,
    @SerializedName("name") var name: String? = null
)