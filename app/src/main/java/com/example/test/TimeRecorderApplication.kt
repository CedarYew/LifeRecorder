package com.example.test

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.test.data.database.AppDatabase
import java.util.*

class TimeRecorderApplication : Application() {
    lateinit var database: AppDatabase
        private set

    // 定义从版本1到版本2的迁移
    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // 创建临时表
            database.execSQL("""
                CREATE TABLE tasks_temp (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    name TEXT NOT NULL,
                    description TEXT NOT NULL,
                    planStart INTEGER NOT NULL,
                    startTime TEXT,
                    endTime TEXT,
                    createTime TEXT NOT NULL,
                    updateTime TEXT NOT NULL,
                    date INTEGER NOT NULL DEFAULT ${getCurrentDateAsInt()}
                )
            """)
            
            // 复制数据
            database.execSQL("""
                INSERT INTO tasks_temp (id, name, description, planStart, startTime, endTime, createTime, updateTime)
                SELECT id, name, description, planStart, startTime, endTime, createTime, updateTime
                FROM tasks
            """)
            
            // 删除旧表
            database.execSQL("DROP TABLE tasks")
            
            // 重命名新表
            database.execSQL("ALTER TABLE tasks_temp RENAME TO tasks")
        }

        private fun getCurrentDateAsInt(): Int {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            return year * 10000 + month * 100 + day
        }
    }

    private val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS daily_data (
                    date INTEGER PRIMARY KEY NOT NULL,
                    weather TEXT NOT NULL DEFAULT 'Sunny',
                    mood TEXT NOT NULL DEFAULT 'Happy',
                    sleep TEXT NOT NULL DEFAULT 'Good',
                    income REAL NOT NULL DEFAULT 0,
                    expenditure REAL NOT NULL DEFAULT 0,
                    totalBalance REAL NOT NULL DEFAULT 0
                )
            """)
        }
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "time_recorder_database"
        )
        .fallbackToDestructiveMigration()
        .build()
    }
} 