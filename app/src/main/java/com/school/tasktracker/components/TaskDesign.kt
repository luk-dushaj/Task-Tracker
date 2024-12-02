package com.school.tasktracker.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskDesign(modifier: Modifier = Modifier, title: String, days: Int, color: Color, edit: Boolean = false) {
    val displayTitle = shrinkTitle(title)
    Box(
        modifier = modifier
            .height(175.dp)
            .width(135.dp)
            .background(color = color)
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
                        modifier = modifier.offset(
                            x = if (displayTitle.length > 6) 2.dp else 5.dp,
                        )
                    )
                }
            }
            Text(
                text = "Days left: $days",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

fun shrinkTitle(title: String):String {
    if (title.length > 6) {
        return "${title.take(6)}..."
    }
    return title
}