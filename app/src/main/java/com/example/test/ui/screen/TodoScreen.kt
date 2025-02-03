package com.example.test.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.test.R
import com.example.test.data.model.Task
import com.example.test.ui.dialog.EditTaskDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(
    tasks: List<Task>,
    onAddTask: () -> Unit,
    onEditTask: (Task) -> Unit,
    onDeleteTask: (Int) -> Unit,
    onToggleStart: (Task) -> Unit,
    onToggleEnd: (Task) -> Unit,
    onSaveTask: (Task) -> Unit,
    showEditDialog: Boolean,
    currentEditTask: Task?,
    onDismissDialog: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ToDo List") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTask
            ) {
                Text("+")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 任务列表
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tasks) { task ->
                    TodoTaskItem(
                        task = task,
                        onEdit = { onEditTask(task) },
                        onDelete = { onDeleteTask(task.id) },
                        onToggleStart = { onToggleStart(task) },
                        onToggleEnd = { onToggleEnd(task) }
                    )
                }
            }
        }

        // 编辑对话框
        if (showEditDialog) {
            EditTaskDialog(
                task = currentEditTask,
                onDismiss = onDismissDialog,
                onConfirm = { task ->
                    // 确保新建的 ToDo 任务的 date 为 -1
                    onSaveTask(task.copy(date = -1))
                },
                currentDate = -1
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TodoTaskItem(
    task: Task,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleStart: () -> Unit,
    onToggleEnd: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onEdit
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 任务标题和描述
            Text(
                text = task.name,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = task.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 开始和结束时间
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Checkbox(
                        checked = task.startTime != null,
                        onCheckedChange = { onToggleStart() }
                    )
                    Text(
                        text = task.startTime ?: "Start",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                
                Row {
                    Checkbox(
                        checked = task.endTime != null,
                        onCheckedChange = { onToggleEnd() }
                    )
                    Text(
                        text = task.endTime ?: "End",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                
                TextButton(onClick = onDelete) {
                    Text("Delete")
                }
            }
        }
    }
}