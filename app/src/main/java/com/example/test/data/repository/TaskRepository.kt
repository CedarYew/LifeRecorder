package com.example.test.data.repository

import com.example.test.data.dao.TaskDao
import com.example.test.data.model.Task
import kotlinx.coroutines.flow.Flow
import java.util.*

class TaskRepository(private val taskDao: TaskDao) {
    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()

    fun getTasksByDate(date: Int): Flow<List<Task>> {
        return taskDao.getTasksByDate(date)
    }

    suspend fun insert(task: Task) {
        taskDao.insert(task)
    }

    suspend fun update(task: Task) {
        taskDao.update(task)
    }

    suspend fun delete(taskId: Int) {
        taskDao.delete(taskId)
    }
} 