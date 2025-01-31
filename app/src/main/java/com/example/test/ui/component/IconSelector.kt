package com.example.test.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun <T> IconSelector(
    options: Array<T>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit,
    getIcon: (T) -> ImageVector,
    getLabel: (T) -> String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        options.forEach { option ->
            val isSelected = option == selectedOption
            IconButton(
                onClick = { onOptionSelected(option) },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = getIcon(option),
                    contentDescription = getLabel(option),
                    tint = if (isSelected) MaterialTheme.colorScheme.primary 
                           else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            }
        }
    }
} 