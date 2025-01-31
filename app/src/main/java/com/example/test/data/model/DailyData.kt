package com.example.test.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_data")
data class DailyData(
    @PrimaryKey
    val date: Int, // 使用与Task相同的日期格式，如20240131
    val weather: String = "Sunny",
    val mood: String = "Happy",
    val sleep: String = "Good",
    val income: Double = 0.0,
    val expenditure: Double = 0.0,
    val totalBalance: Double = 0.0
) 