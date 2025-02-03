package com.example.test.ui.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.test.data.model.Task
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskDialog(
    task: Task?,
    onDismiss: () -> Unit,
    onConfirm: (Task) -> Unit,
    currentDate: Int
) {
    val isNewTask = task == null
    var name by remember { mutableStateOf(task?.name ?: "") }
    var description by remember { mutableStateOf(task?.description ?: "") }
    var planStart by remember { mutableStateOf(task?.planStart?.toString() ?: "9") }
    var startTime by remember { 
        mutableStateOf(
            task?.startTime?.let { time ->
                try {
                    val fullFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
                    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                    val date = fullFormat.parse(time)
                    timeFormat.format(date)
                } catch (e: Exception) {
                    time // 如果解析失败，保留原始值
                }
            }
        )
    }
    var endTime by remember { 
        mutableStateOf(
            task?.endTime?.let { time ->
                try {
                    val fullFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
                    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                    val date = fullFormat.parse(time)
                    timeFormat.format(date)
                } catch (e: Exception) {
                    time // 如果解析失败，保留原始值
                }
            }
        )
    }
    var date by remember { mutableStateOf(task?.date ?: currentDate) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isNewTask) "新建任务" else "编辑任务") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 任务名称
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("任务名称") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // 任务描述
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("任务描述") },
                    modifier = Modifier.fillMaxWidth()
                )

                // 计划开始时间
                OutlinedTextField(
                    value = planStart,
                    onValueChange = { 
                        if (it.isEmpty() || it.toIntOrNull() in 0..23) {
                            planStart = it 
                        }
                    },
                    label = { Text("计划开始时间 (0-23)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // 日期选择
                OutlinedButton(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(formatDate(date))
                }

                // 开始时间
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("开始时间: ${startTime ?: "未开始"}")
                    Row {
                        TextButton(onClick = { showStartTimePicker = true }) {
                            Text("选择时间")
                        }
                        TextButton(onClick = { startTime = null }) {
                            Text("清除")
                        }
                    }
                }

                // 结束时间
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("结束时间: ${endTime ?: "未结束"}")
                    Row {
                        TextButton(onClick = { showEndTimePicker = true }) {
                            Text("选择时间")
                        }
                        TextButton(onClick = { endTime = null }) {
                            Text("清除")
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(
                        Task(
                            id = task?.id ?: 0,
                            name = name,
                            description = description,
                            planStart = planStart.toIntOrNull() ?: 0,
                            startTime = startTime?.let { time ->
                                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
                                    .format(
                                        SimpleDateFormat("HH:mm", Locale.getDefault())
                                            .parse(time) ?: Date()
                                    )
                            },
                            endTime = endTime?.let { time ->
                                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
                                    .format(
                                        SimpleDateFormat("HH:mm", Locale.getDefault())
                                            .parse(time) ?: Date()
                                    )
                            },
                            date = date
                        )
                    )
                },
                enabled = name.isNotBlank()
            ) {
                Text("确定")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )

    // 日期选择器
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            onDateSelected = { selectedDate ->
                date = convertDateToInt(selectedDate)
                showDatePicker = false
            }
        )
    }

    // 开始时间选择器
    if (showStartTimePicker) {
        TimePickerDialog(
            onDismiss = { showStartTimePicker = false },
            onTimeSelected = { hour, minute ->
                startTime = String.format("%02d:%02d", hour, minute)
                showStartTimePicker = false
            }
        )
    }

    // 结束时间选择器
    if (showEndTimePicker) {
        TimePickerDialog(
            onDismiss = { showEndTimePicker = false },
            onTimeSelected = { hour, minute ->
                endTime = String.format("%02d:%02d", hour, minute)
                showEndTimePicker = false
            }
        )
    }
}

private fun formatDate(dateInt: Int): String {
    val year = dateInt / 10000
    val month = (dateInt % 10000) / 100
    val day = dateInt % 100
    return String.format("%d年%d月%d日", year, month, day)
}

private fun convertDateToInt(date: Date): Int {
    val calendar = Calendar.getInstance().apply {
        time = date
    }
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    return year * 10000 + month * 100 + day
} 