package com.example.test.data.dao

import androidx.room.*
import com.example.test.data.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY planStart ASC, createTime ASC")
    fun getAllTasks(): Flow<List<Task>>

    @Insert
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun delete(taskId: Int)
} 