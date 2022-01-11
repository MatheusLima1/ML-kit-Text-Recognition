package com.example.textrecognition

import android.app.Application
import androidx.room.Room
import com.example.textrecognition.db.database.AppDataBase

class TextInfoApplication : Application() {
    companion object {
        var database: AppDataBase? = null
    }

    override fun onCreate() {
        super.onCreate()

        database =
            Room.databaseBuilder(this, AppDataBase::class.java, "my-db").allowMainThreadQueries()
                .build()
    }

}