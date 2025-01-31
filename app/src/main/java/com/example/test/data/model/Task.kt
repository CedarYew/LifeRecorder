package com.example.test.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val planStart: Int = 0,
    val startTime: String? = null,
    val endTime: String? = null,
    val createTime: String = Date().toString(),
    val updateTime: String = Date().toString(),
    val date: Int // 例如：20240131 表示2024年1月31日
) 