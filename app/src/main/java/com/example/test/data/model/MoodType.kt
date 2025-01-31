package com.example.test.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.SentimentNeutral
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.SentimentVeryDissatisfied
import androidx.compose.material.icons.filled.SentimentVerySatisfied
import androidx.compose.ui.graphics.vector.ImageVector

enum class MoodType(val value: Int, val icon: ImageVector, val label: String) {
    DEVASTATED(-2, Icons.Default.SentimentVeryDissatisfied, "大哭"),
    SAD(-1, Icons.Default.SentimentDissatisfied, "流泪"),
    NEUTRAL(0, Icons.Default.SentimentNeutral, "微笑"),
    HAPPY(1, Icons.Default.SentimentSatisfied, "愉快"),
    ECSTATIC(2, Icons.Default.SentimentVerySatisfied, "大笑")
} 