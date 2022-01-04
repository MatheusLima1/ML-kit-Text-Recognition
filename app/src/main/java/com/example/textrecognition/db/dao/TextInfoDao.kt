package com.example.textrecognition.db.dao

import androidx.room.*
import com.example.textrecognition.db.entity.TextInfo

@Dao
interface TextInfoDao {
    @Query("SELECT * FROM textinfo") fun getAllText() : List<TextInfo>

    @Query("SELECT * FROM textinfo WHERE text like :textDescription") fun getTextInfoWithDescription(textDescription: String) : List<TextInfo>

    @Query("SELECT * FROM textinfo WHERE isSync = 0") fun getTextNotSynced() : List<TextInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE) fun insertTextInfo(vararg textInfo: TextInfo)

    @Update fun updateTextInfo(textInfo: TextInfo)

    @Delete fun deleteTextInfo(textInfo: TextInfo)
}