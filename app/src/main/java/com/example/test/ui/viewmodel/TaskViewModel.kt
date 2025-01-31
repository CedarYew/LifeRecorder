package com.example.test.ui.viewmodel

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test.data.model.Task
import com.example.test.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {
    private val _currentDate = MutableStateFlow(getCurrentDateAsInt())
    val currentDate: StateFlow<Int> = _currentDate.asStateFlow()

    val currentTasks: StateFlow<List<Task>> = currentDate.flatMapLatest { date ->
        repository.getTasksByDate(date)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun updateCurrentDate(timestamp: Long) {
        _currentDate.value = convertTimestampToDateInt(timestamp)
    }

    fun addTask(task: Task) = viewModelScope.launch {
        repository.insert(task.copy(date = currentDate.value))
    }

    fun updateTask(task: Task) = viewModelScope.launch {
        repository.update(task)
    }

    fun deleteTask(taskId: Int) = viewModelScope.launch {
        repository.delete(taskId)
    }

    private fun getCurrentDateAsInt(): Int {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH 从0开始
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return year * 10000 + month * 100 + day
    }

    private fun convertTimestampToDateInt(timestamp: Long): Int {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = timestamp
        }
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return year * 10000 + month * 100 + day
    }
} 