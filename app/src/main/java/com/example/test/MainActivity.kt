package com.example.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.test.data.model.Task
import com.example.test.data.repository.TaskRepository
import com.example.test.data.repository.DailyDataRepository
import com.example.test.ui.screen.MainScreen
import com.example.test.ui.theme.TestTheme
import com.example.test.ui.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    private val viewModel: TaskViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val database = (application as TimeRecorderApplication).database
                val taskRepository = TaskRepository(database.taskDao())
                val dailyDataRepository = DailyDataRepository(database.dailyDataDao())
                @Suppress("UNCHECKED_CAST")
                return TaskViewModel(taskRepository, dailyDataRepository) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestTheme {
                val tasks by viewModel.currentTasks.collectAsState()
                val todoTasks by viewModel.todoTasks.collectAsState()
                val currentDate by viewModel.currentDate.collectAsState()
                val dailyData by viewModel.currentDailyData.collectAsState()
                var showEditDialog by remember { mutableStateOf(false) }
                var currentEditTask by remember { mutableStateOf<Task?>(null) }
                
                MainScreen(
                    tasks = tasks,
                    todoTasks = todoTasks,
                    dailyData = dailyData,
                    onAddTask = {
                        showEditDialog = true
                        currentEditTask = null
                    },
                    onEditTask = { task ->
                        showEditDialog = true
                        currentEditTask = task
                    },
                    onDeleteTask = { taskId ->
                        viewModel.deleteTask(taskId)
                    },
                    onToggleStart = { task ->
                        viewModel.updateTask(
                            task.copy(
                                startTime = if (task.startTime == null)
                                    SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                                else null
                            )
                        )
                    },
                    onToggleEnd = { task ->
                        viewModel.updateTask(
                            task.copy(
                                endTime = if (task.endTime == null)
                                    SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                                else null
                            )
                        )
                    },
                    onSaveTask = { task ->
                        if (task.id == 0) {
                            viewModel.addTask(task)
                        } else {
                            viewModel.updateTask(task)
                        }
                        showEditDialog = false
                        currentEditTask = null
                    },
                    showEditDialog = showEditDialog,
                    currentEditTask = currentEditTask,
                    onDismissDialog = {
                        showEditDialog = false
                        currentEditTask = null
                    },
                    currentDate = currentDate,
                    onDateSelected = { date ->
                        viewModel.updateCurrentDate(date)
                    },
                    onUpdateDailyData = { updatedDailyData ->
                        viewModel.updateDailyData(updatedDailyData)
                    }
                )
            }
        }
    }
}