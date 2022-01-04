package com.example.textrecognition.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "textinfo")
class TextInfo(
    var text: String,
    var deviceUuid: String,
    var isSync: Boolean
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}