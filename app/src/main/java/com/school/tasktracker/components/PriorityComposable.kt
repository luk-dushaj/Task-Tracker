package com.school.tasktracker.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.school.tasktracker.data.MainViewModel
import com.school.tasktracker.data.Routes

@Composable
// This is a composable to hold all task priority objects.
// By default is used for HomeView with onClick opening a DetailView
// Editable is for the SelectionView and which leads to EditView
fun PriorityComposable(
    modifier: Modifier = Modifier,
    isPriority: Boolean,
    viewModel: MainViewModel,
    navController: NavController,
    editable: Boolean = false
) {
    val tasks = viewModel.tasks
    Column {
        Row {
            TextRow(
                color = Color.Gray,
                weight = FontWeight.ExtraBold,
                title = if (isPriority) "High Priority" else "Low Priority"
            )
            Spacer(modifier = modifier.width(4.dp))
            if (isPriority) {
                HalfStarIcon(filled = true)
            }
        }
        Spacer(modifier = modifier.height(5.dp))
        ColoredLine(color = if (isPriority) Color.Red else Color.Blue)
        Spacer(
            modifier = modifier.height(5.dp)
        )
    }
    LazyRow (
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(tasks.value!!) { item ->
            if (editable) {
                TaskDesign(
                    modifier = modifier
                        .clickable {
                            viewModel.selectedTask = item
                            navController.navigate(Routes.edit)
                        },
                    title = item.title,
                    days = 0,
                    color = if (isPriority) Color(
                        red = 245,
                        green = 104,
                        blue = 115
                    ) else Color(red = 115, green = 152, blue = 245),
                    edit = true
                )
            } else {
                TaskDesign(
                    modifier = modifier
                        .clickable {
                            viewModel.selectedTask = item
                            navController.navigate(Routes.detail)
                        },
                    title = item.title,
                    days = 0,
                    color = if (isPriority) Color(
                        red = 245,
                        green = 104,
                        blue = 115
                    ) else Color(red = 115, green = 152, blue = 245)
                )
            }
        }
    }
}