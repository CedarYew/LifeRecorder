package com.example.test

import android.app.Application
import androidx.room.Room
import com.example.test.data.database.AppDatabase

class TimeRecorderApplication : Application() {
    lateinit var database: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "time_recorder_database"
        ).build()
    }
} 