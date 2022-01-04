package com.example.textrecognition.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.textrecognition.db.dao.TextInfoDao
import com.example.textrecognition.db.entity.TextInfo

@Database(version = 1, entities = [TextInfo::class], exportSchema = false)
abstract class AppDataBase: RoomDatabase() {
    abstract fun textInfoDao(): TextInfoDao
}