package com.example.mydiaryproject.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "diaries")
data class DiaryDb(
    @ColumnInfo(name = "date")
    @PrimaryKey
    var date: String = "",
    @ColumnInfo(name = "title")
    var title: String = "",
    @ColumnInfo(name = "imageUrl")
    var imageUrl: String = "",
    @ColumnInfo(name = "password")
    var password: String = "",
    @ColumnInfo(name = "description")
    var description: String = "",
    @ColumnInfo(name = "personnelMood")
    var personnelMood: String = ""
)