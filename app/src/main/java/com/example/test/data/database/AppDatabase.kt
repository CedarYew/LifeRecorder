package com.example.test.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.test.data.dao.TaskDao
import com.example.test.data.dao.DailyDataDao
import com.example.test.data.model.Task
import com.example.test.data.model.DailyData

@Database(
    entities = [Task::class, DailyData::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun dailyDataDao(): DailyDataDao
} 