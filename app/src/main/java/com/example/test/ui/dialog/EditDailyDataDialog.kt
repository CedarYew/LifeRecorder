package com.example.test.ui.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.test.data.model.DailyData
import com.example.test.data.model.WeatherType
import com.example.test.data.model.MoodType
import com.example.test.data.model.SleepType
import com.example.test.ui.component.IconSelector

@Composable
fun EditDailyDataDialog(
    dailyData: DailyData,
    onDismiss: () -> Unit,
    onConfirm: (DailyData) -> Unit
) {
    var weather by remember { mutableStateOf(dailyData.weather) }
    var mood by remember { mutableStateOf(dailyData.mood) }
    var sleep by remember { mutableStateOf(dailyData.sleep) }
    var income by remember { mutableStateOf(dailyData.income.toString()) }
    var expenditure by remember { mutableStateOf(dailyData.expenditure.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Daily Info") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column {
                    Text(
                        text = "Weather",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    IconSelector(
                        options = WeatherType.values(),
                        selectedOption = weather,
                        onOptionSelected = { weather = it },
                        getIcon = { it.icon },
                        getLabel = { it.label }
                    )
                }

                Column {
                    Text(
                        text = "Mood",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    IconSelector(
                        options = MoodType.values(),
                        selectedOption = mood,
                        onOptionSelected = { mood = it },
                        getIcon = { it.icon },
                        getLabel = { it.label }
                    )
                }

                Column {
                    Text(
                        text = "Sleep",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    IconSelector(
                        options = SleepType.values(),
                        selectedOption = sleep,
                        onOptionSelected = { sleep = it },
                        getIcon = { it.icon },
                        getLabel = { it.label }
                    )
                }

                OutlinedTextField(
                    value = income,
                    onValueChange = { if (it.isEmpty() || it.toDoubleOrNull() != null) income = it },
                    label = { Text("Income") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = expenditure,
                    onValueChange = { if (it.isEmpty() || it.toDoubleOrNull() != null) expenditure = it },
                    label = { Text("Expenditure") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val updatedDailyData = dailyData.copy(
                        weather = weather,
                        mood = mood,
                        sleep = sleep,
                        income = income.toDoubleOrNull() ?: 0.0,
                        expenditure = expenditure.toDoubleOrNull() ?: 0.0,
                        totalBalance = (income.toDoubleOrNull() ?: 0.0) - (expenditure.toDoubleOrNull() ?: 0.0)
                    )
                    onConfirm(updatedDailyData)
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
} 