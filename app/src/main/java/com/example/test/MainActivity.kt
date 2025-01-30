package com.example.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.test.data.model.Task
import com.example.test.data.repository.TaskRepository
import com.example.test.ui.screen.MainScreen
import com.example.test.ui.theme.TestTheme
import com.example.test.ui.viewmodel.TaskViewModel
import java.time.LocalDateTime

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
                
                MainScreen(
                    tasks = tasks,
                    onAddTask = {
                        // 创建新任务
                        viewModel.addTask(
                            Task(
                                name = "新任务",
                                description = "请编辑任务描述",
                                planStart = LocalDateTime.now().hour
                            )
                        )
                    },
                    onEditTask = { task ->
                        // TODO: 显示编辑对话框
                    },
                    onDeleteTask = { taskId ->
                        viewModel.deleteTask(taskId)
                    },
                    onToggleStart = { task ->
                        viewModel.updateTask(
                            task.copy(
                                startTime = if (task.startTime == null) 
                                    LocalDateTime.now().toString() 
                                else null
                            )
                        )
                    },
                    onToggleEnd = { task ->
                        viewModel.updateTask(
                            task.copy(
                                endTime = if (task.endTime == null) 
                                    LocalDateTime.now().toString() 
                                else null
                            )
                        )
                    }
                )
            }
        }
    }
}