package com.example.test.ui.viewmodel

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test.data.model.Task
import com.example.test.data.repository.TaskRepository
import com.example.test.data.model.DailyData
import com.example.test.data.repository.DailyDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

class TaskViewModel(
    private val taskRepository: TaskRepository,
    private val dailyDataRepository: DailyDataRepository
) : ViewModel() {
    private val _currentDate = MutableStateFlow(getCurrentDateAsInt())
    val currentDate: StateFlow<Int> = _currentDate.asStateFlow()

    val currentTasks: StateFlow<List<Task>> = _currentDate
        .flatMapLatest { date ->
            taskRepository.getTasksByDate(date)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val todoTasks: StateFlow<List<Task>> = taskRepository
        .getTasksByDate(-1)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val currentDailyData: StateFlow<DailyData> = _currentDate
        .flatMapLatest { date ->
            dailyDataRepository.getDailyDataByDate(date)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DailyData(date = getCurrentDateAsInt())
        )

    fun updateCurrentDate(timestamp: Long) {
        _currentDate.value = convertTimestampToDateInt(timestamp)
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            taskRepository.insert(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            taskRepository.update(task)
        }
    }

    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            taskRepository.delete(taskId)
        }
    }

    fun updateDailyData(dailyData: DailyData) {
        viewModelScope.launch {
            dailyDataRepository.insertOrUpdate(dailyData)
        }
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