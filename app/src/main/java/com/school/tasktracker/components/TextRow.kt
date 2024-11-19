package com.school.tasktracker.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

@Composable
fun TextRow(
    title: String,
    style: TextStyle = MaterialTheme.typography.headlineSmall,
    weight: FontWeight = FontWeight.Normal,
    color: Color = Color.Black,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = style,
        fontWeight = weight,
        color = color,
        modifier = modifier
    )
}