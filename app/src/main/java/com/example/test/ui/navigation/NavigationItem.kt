package com.example.test.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.ui.graphics.vector.ImageVector

enum class NavigationItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    TODAY("today", Icons.Default.Today, "Today"),
    TODO("todo", Icons.Default.Assignment, "ToDo"),
    ME("me", Icons.Default.AccountCircle, "Me")
} 