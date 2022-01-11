package com.example.textrecognition.network.entity

import com.google.gson.annotations.SerializedName

data class TextInfoNetwork (
    @SerializedName("id"         ) var id         : String?             = null,
    @SerializedName("clientId"   ) var clientId   : String?             = null,
    @SerializedName("company"    ) var company    : Company?            = Company(),
    @SerializedName("client"     ) var client     : Client?             = Client(),
    @SerializedName("products"   ) var products   : ArrayList<Products> = arrayListOf(),
    @SerializedName("data"       ) var data       : String?             = null,
    @SerializedName("total"      ) var total      : Int?                = null,
    @SerializedName("textReceip" ) var textReceip : String?             = null
)