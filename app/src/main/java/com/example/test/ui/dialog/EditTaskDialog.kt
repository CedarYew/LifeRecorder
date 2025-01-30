package com.example.test.ui.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.test.data.model.Task
import java.util.Calendar
import java.util.Date

@Composable
fun EditTaskDialog(
    task: Task?,
    onDismiss: () -> Unit,
    onConfirm: (Task) -> Unit
) {
    var name by remember { mutableStateOf(task?.name ?: "") }
    var description by remember { mutableStateOf(task?.description ?: "") }
    var planStart by remember {
        mutableStateOf(task?.planStart?.toString() ?:
            Calendar.getInstance().get(Calendar.HOUR_OF_DAY).toString())
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (task == null) "New Task" else "Edit Task") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Task Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = planStart,
                    onValueChange = { 
                        if (it.isEmpty() || it.toIntOrNull() in 0..23) {
                            planStart = it
                        }
                    },
                    label = { Text("Planned Start Time (0-23)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val startHour = planStart.toIntOrNull() ?:
                        Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                    val newTask = Task(
                        id = task?.id ?: 0,
                        name = name.takeIf { it.isNotBlank() } ?: "Task name",
                        description = description.takeIf { it.isNotBlank() } ?: "Task description",
                        planStart = startHour,
                        startTime = task?.startTime,
                        endTime = task?.endTime,
                        createTime = task?.createTime ?: Date().toString(),
                        updateTime = Date().toString()
                    )
                    onConfirm(newTask)
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
} 