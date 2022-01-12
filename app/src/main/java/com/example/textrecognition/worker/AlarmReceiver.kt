package com.example.textrecognition.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.textrecognition.R
import com.example.textrecognition.TextInfoApplication
import com.example.textrecognition.network.entity.Receipt
import com.example.textrecognition.network.webclient.TextInfoWebClient
import android.net.NetworkInfo

import android.net.ConnectivityManager


class AlarmReceiver() : BroadcastReceiver() {

    companion object {
        const val NOTIFICATION_ID = 100
        const val NOTIFICATION_CHANNEL_ID = "1000"
    }

    override fun onReceive(context: Context, intent: Intent) {

        createNotificationChannel(context)
        notifyNotification(context)

        val textInfoDao = TextInfoApplication.database?.textInfoDao()

        textInfoDao?.getTextNotSynced()?.forEach { textInfo ->
            TextInfoWebClient().process(
                Receipt(textInfo.text, textInfo.deviceUuid),
                preExecute = { Log.i("Receiver pre-execute", "executou") },
                finished = { Log.i("Receiver finish", "finalizou") },
                sucess = {
                    textInfo.isSync = true
                    textInfoDao.updateTextInfo(textInfo)
                    Log.i("Receiver SUCESS", "finalizou")
                },
                failure = {
                    Log.i("Receiver failure", "falhou", it)
                    Toast.makeText(
                        context,
                        "Falha ao enviar os dados para o servidor",
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
        }
    }

    private fun createNotificationChannel(context: Context) {

        val notificationChannel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "Sincronizando dados",
            NotificationManager.IMPORTANCE_HIGH
        )
        NotificationManagerCompat.from(context).createNotificationChannel(notificationChannel)
    }

    private fun notifyNotification(context: Context) {
        with(NotificationManagerCompat.from(context)) {
            val build = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setContentTitle("Sincronizando dados")
                .setContentText("Enviando dados locais para nuvem")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

            notify(NOTIFICATION_ID, build.build())
        }
    }
}