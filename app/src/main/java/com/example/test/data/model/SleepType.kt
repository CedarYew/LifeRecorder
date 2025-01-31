package com.example.test.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryAlert
import androidx.compose.material.icons.filled.Battery2Bar
import androidx.compose.material.icons.filled.Battery6Bar
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.ui.graphics.vector.ImageVector

enum class SleepType(val icon: ImageVector, val label: String) {
    SEVERE_LACK(Icons.Default.BatteryAlert, "极度缺乏"),
    LACK(Icons.Default.Battery2Bar, "缺乏"),
    NORMAL(Icons.Default.Battery6Bar, "正常"),
    SUFFICIENT(Icons.Default.BatteryFull, "充足"),
    ABUNDANT(Icons.Default.BatteryChargingFull, "非常充沛")
} 