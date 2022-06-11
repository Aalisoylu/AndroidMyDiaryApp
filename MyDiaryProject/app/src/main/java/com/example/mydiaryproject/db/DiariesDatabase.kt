package com.example.mydiaryproject.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mydiaryproject.db.entity.DiaryDb


@Database(
    entities = [DiaryDb::class],
    version = 1
)
abstract class DiariesDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var instance: DiariesDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                DiariesDatabase::class.java,
                "diaries.db"
            ).build()

    }

    abstract fun dairyDao(): DiaryDao
}