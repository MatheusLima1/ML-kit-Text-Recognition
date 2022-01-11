package com.example.textrecognition.network

interface CallBackResponse<T> {

    fun sucess(response: T)
}