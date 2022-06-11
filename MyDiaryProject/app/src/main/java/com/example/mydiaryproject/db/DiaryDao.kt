package com.example.mydiaryproject.db

import androidx.room.*
import com.example.mydiaryproject.db.entity.DiaryDb

@Dao
interface DiaryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertDiary(diaryDb: DiaryDb)

    @Query("SELECT * FROM diaries")
    fun getDiaryList(): List<DiaryDb>

}