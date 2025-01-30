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
import com.example.test.ui.screen.MainScreen
import com.example.test.ui.theme.TestTheme
import com.example.test.ui.viewmodel.TaskViewModel
import java.util.*

class MainActivity : ComponentActivity() {
    private val viewModel: TaskViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val database = (application as TimeRecorderApplication).database
                val repository = TaskRepository(database.taskDao())
                @Suppress("UNCHECKED_CAST")
                return TaskViewModel(repository) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestTheme {
                val tasks by viewModel.allTasks.collectAsState(initial = emptyList())
                var showEditDialog by remember { mutableStateOf(false) }
                var currentEditTask by remember { mutableStateOf<Task?>(null) }
                
                MainScreen(
                    tasks = tasks,
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
                                    Date().toString() 
                                else null
                            )
                        )
                    },
                    onToggleEnd = { task ->
                        viewModel.updateTask(
                            task.copy(
                                endTime = if (task.endTime == null) 
                                    Date().toString() 
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
                    }
                )
            }
        }
    }
}