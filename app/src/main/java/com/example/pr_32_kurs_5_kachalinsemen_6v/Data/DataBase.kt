package com.example.pr_32_kurs_5_kachalinsemen_6v.Data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Weater::class], version = 1, exportSchema = false)
abstract class DataBase : RoomDatabase() {

    abstract fun weater_Helper(): Weater_Helper

    companion object {
        @Volatile private var obj: DataBase? = null

        fun getInstance(context: Context): DataBase =
            obj ?: synchronized(this) {
                obj ?: Room.databaseBuilder(
                    context.applicationContext,
                    DataBase::class.java,
                    "my_weather4.db"
                ).build().also { obj = it }
            }
    }
}