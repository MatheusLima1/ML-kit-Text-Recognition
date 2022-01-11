package com.example.textrecognition.network.webclient

import com.example.textrecognition.network.RetrofitInicializr
import com.example.textrecognition.network.callback.callback
import com.example.textrecognition.network.entity.Receipt
import com.example.textrecognition.network.entity.TextInfoNetwork
import com.example.textrecognition.network.util.defaultFailure
import com.example.textrecognition.network.util.defaultResponse

class TextInfoWebClient {
    fun list(
        preExecute:()->Unit = {},
        finished:()->Unit = {},
        sucess: (note: List<TextInfoNetwork>) -> Unit,
        failure: (throwable: Throwable) -> Unit,
        uuid: String
    ) {
        val call = RetrofitInicializr().textInfoService().listReceipt(uuid)
        call.enqueue(callback(
            sucess = {it.defaultResponse(sucess)},
            failure = { it.defaultFailure(failure) },
            preExecute = preExecute,
            finished = finished))
    }

    fun getReceipt(
        preExecute:()->Unit = {},
        finished:()->Unit = {},
        sucess: (note: TextInfoNetwork) -> Unit,
        failure: (throwable: Throwable) -> Unit,
        uuid: String,
        id: String
    ) {
        val call = RetrofitInicializr().textInfoService().getReceipt(uuid, id)
        call.enqueue(callback(
            sucess = {it.defaultResponse(sucess)},
            failure = { it.defaultFailure(failure) },
            preExecute = preExecute,
            finished = finished))
    }

    fun process(receipt: Receipt,
               preExecute: () -> Unit,
               finished:() -> Unit,
               sucess: (receipt: TextInfoNetwork) -> Unit,
               failure: (throwable: Throwable) -> Unit) {
        val call = RetrofitInicializr().textInfoService().processTextInfo(receipt)
        call.enqueue(callback(
            sucess = {it.defaultResponse(sucess)},
            failure = { it.defaultFailure(failure) },
            preExecute = preExecute,
            finished = finished))
    }
}