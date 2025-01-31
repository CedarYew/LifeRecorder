package com.example.test.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.Umbrella
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.ui.graphics.vector.ImageVector

enum class WeatherType(val icon: ImageVector, val label: String) {
    SUNNY(Icons.Default.WbSunny, "晴天"),
    CLOUDY(Icons.Default.Cloud, "多云"),
    OVERCAST(Icons.Default.CloudQueue, "阴天"),
    LIGHT_RAIN(Icons.Default.Umbrella, "小雨"),
    HEAVY_RAIN(Icons.Default.Thunderstorm, "大雨"),
    SNOW(Icons.Default.AcUnit, "雪")
} 