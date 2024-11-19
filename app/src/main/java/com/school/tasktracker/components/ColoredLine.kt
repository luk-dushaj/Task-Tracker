package com.school.tasktracker.components

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ColoredLine(modifier: Modifier = Modifier, color: Color) {
    HorizontalDivider(
        modifier = modifier
            .width(30.dp)
            .offset(x = (-5).dp),
        color = color,
        thickness = 2.dp
    )
}