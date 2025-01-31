package com.example.test.ui.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.test.data.model.DailyData

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
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = weather,
                    onValueChange = { weather = it },
                    label = { Text("Weather") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = mood,
                    onValueChange = { mood = it },
                    label = { Text("Mood") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = sleep,
                    onValueChange = { sleep = it },
                    label = { Text("Sleep") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

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