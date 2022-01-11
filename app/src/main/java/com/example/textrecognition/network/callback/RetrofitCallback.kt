package com.example.textrecognition.network.callback

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun <T> callback(preExecute: () -> Unit = {},
                 finished:() -> Unit = {},
                 sucess: (response: Response<T>?) -> Unit,
                 failure: (throwable: Throwable?) -> Unit): Callback<T> {
    preExecute()
    return object : Callback<T> {
        override fun onResponse(call: Call<T>?, response: Response<T>?) {
            sucess(response)
            finished()
        }

        override fun onFailure(call: Call<T>?, t: Throwable?) {
           failure(t)
           finished()
        }
    }
}
