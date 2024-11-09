package com.school.tasktracker.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.school.tasktracker.data.MainViewModel

@Composable
fun TodoView(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    // Separate between priority lists
    val distance = Modifier.offset(y = 165.dp)
    Column {
        // Have to wrap priorites text in a box
        // So I can easily dyanmically center it
        Box (
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Priorities",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
        }
        TodoType(
            modifier = Modifier,
            text = "High",
            textColor = Color.Black,
            boxColor = Color.Gray
        )
        TodoType(
            modifier = distance,
            text = "Low",
            textColor = Color.Black,
            boxColor = Color.Gray
        )
    }
}

@Composable
// Like Top priority, Low priority
fun TodoType(modifier: Modifier = Modifier, text: String, textColor: Color, boxColor: Color) {
    Column (
        modifier = modifier.offset(y = 35.dp)
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(color = boxColor)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.headlineSmall,
                color = textColor,
                fontWeight = FontWeight.Bold
            )
        }
        // This is where the Tasks will be placed
        // Eventually will make a Task composable
    }
}