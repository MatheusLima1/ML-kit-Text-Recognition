package com.example.textrecognition.db.dao

import androidx.room.*
import com.example.textrecognition.db.entity.TextInfo

@Dao
interface TextInfoDao {
    @Query("SELECT * FROM textinfo") fun getAllText() : List<TextInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE) fun insertTextInfo(vararg textInfo: TextInfo)

    @Update fun updateTextInfo(textInfo: TextInfo)

    @Delete fun deleteTextInfo(textInfo: TextInfo)
}