package com.example.test.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryAlert
import androidx.compose.material.icons.filled.BatteryStd
import androidx.compose.material.icons.filled.BatteryStd
import androidx.compose.material.icons.filled.BatteryStd
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.ui.graphics.vector.ImageVector

enum class SleepType(val icon: ImageVector, val label: String) {
    TIRED(Icons.Default.BatteryAlert, "困"),
    NORMAL(Icons.Default.BatteryStd, "正常"),
    SUFFICIENT(Icons.Default.BatteryFull, "充足")
} 