package com.school.tasktracker.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.school.tasktracker.ui.theme.TaskTrackerTheme

@Composable
fun TaskDesign(modifier: Modifier = Modifier, title: String, days: Int, color: Color, edit: Boolean = false) {
    val displayTitle = shrinkTitle(title)
    Box(
        modifier = modifier
            .height(175.dp)
            .background(color = color)
            .clip(RoundedCornerShape(8.dp))
    ) {
        Column(
            modifier = modifier
                .padding(15.dp),
            verticalArrangement = Arrangement.spacedBy(75.dp)
        ) {
            Row {
                Text(
                    text = displayTitle,
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                )
                if (edit) {
                    Icon(
                        imageVector = Icons.Filled.Create,
                        contentDescription = "Edit task icon",
                        modifier = modifier.offset(y = (-10).dp)
                    )
                }
            }
            Text(
                text = "Days left: $days days",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

fun shrinkTitle(title: String):String {
    if (title.length >= 10) {
        return "${title.take(7)}..."
    }
    return title
}

@Preview
@Composable
private fun PreviewTaskDesign() {
    TaskTrackerTheme {
        Surface {
            TaskDesign(
                title = "Test",
                days = 0,
                color = Color.Gray,
                edit = true
            )
        }
    }
    
}