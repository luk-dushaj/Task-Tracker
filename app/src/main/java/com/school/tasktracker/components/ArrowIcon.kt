package com.school.tasktracker.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ArrowIcon(modifier: Modifier = Modifier, flip: Boolean) {
    if (!flip) {
        Icon(
            modifier = modifier
                .size(50.dp),
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "RightArrow"
        )
    } else {
        // When InfoType is clicked simulate turn animation
        Icon(
            modifier = modifier
                .size(50.dp),
            imageVector = Icons.Filled.KeyboardArrowDown,
            contentDescription = "DownArrow"
        )
    }
}