package com.example.test.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test.data.model.Task
import com.example.test.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {
    val allTasks = repository.allTasks

    fun addTask(task: Task) = viewModelScope.launch {
        repository.insert(task)
    }

    fun updateTask(task: Task) = viewModelScope.launch {
        repository.update(task)
    }

    fun deleteTask(taskId: Int) = viewModelScope.launch {
        repository.delete(taskId)
    }
} 