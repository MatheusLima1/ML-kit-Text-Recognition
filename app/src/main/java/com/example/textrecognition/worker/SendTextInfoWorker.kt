package com.example.textrecognition.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.textrecognition.TextInfoApplication

class SendTextInfoWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        TextInfoApplication.database?.textInfoDao()?.getTextNotSynced()?.let {
            if(it.isNotEmpty()){
               //TODO Requisição web
            }
        }
        return Result.failure()
    }
}