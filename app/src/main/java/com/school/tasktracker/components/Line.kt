package com.school.tasktracker.components

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun Line(modifier: Modifier = Modifier, color: Color) {
    HorizontalDivider(
        modifier = modifier,
        thickness = 3.dp,
        color = color
    )
}