package com.example.test.data.model

data class DailyData(
    val date: String,
    val weather: String,
    val mood: String,
    val sleep: String,
    val income: Double,
    val expenditure: Double,
    val totalBalance: Double,
    val records: List<Task>
) 