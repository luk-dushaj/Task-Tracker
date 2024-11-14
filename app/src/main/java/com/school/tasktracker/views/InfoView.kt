package com.school.tasktracker.views

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.school.tasktracker.data.InfoItem
import com.school.tasktracker.data.MainViewModel
import com.school.tasktracker.components.*
import com.school.tasktracker.ui.theme.TaskTrackerTheme

@Composable
fun InfoView(modifier: Modifier = Modifier) {

    // When you add a new item to list, make sure you update the list count in viewModels _infoValue attribute
    // viewModel is used here so I can save users state even when they switch to another view

    val infoItems = listOf(
        InfoItem(
            title = "How to Add a Task",
            description = """
                To add a task, click the '+' button located at the bottom-right corner of the Home screen.
            """.trimIndent()
        ),
        InfoItem(
            title = "How to Edit a Task",
            description = """
                First, click the 'Edit' button on the top-left of the Home screen.
                This will make a pencil icon appear next to each task.
                Tap the pencil icon of the task you want to edit, and a new screen will open where you can change the task details.
            """.trimIndent()
        ),
        InfoItem(
            title = "How to Import and Export Tasks",
            description = """
                Go to the Settings screen by clicking the settings icon in the navigation bar. 
                To export your tasks, click the 'Export' button to download your tasks as a JSON file.
                To import tasks, click the 'Import' button, and upload a JSON file with your tasks.
            """.trimIndent()
        ),
        InfoItem(
            title = "How to Change the App Theme",
            description = """
                In the Settings screen, you will see a toggle for Light and Dark mode.
                Tap the toggle to switch between themes.
            """.trimIndent()
        ),
        InfoItem(
            title = "How tasks are organized",
            description = """
                On the home screen, tasks are by default organized:
                By high to low priority (red to blue)
                When the task is due by (closest date)
            """.trimIndent()
        ),
        InfoItem(
            title = "A task that passed it's due date?",
            description = """
                They are automatically deleted for you.
                So you better have push notifications on 😊
            """.trimIndent()
        )
    )
    LazyColumn (
        modifier = modifier.padding(5.dp)
    ) {
        itemsIndexed(infoItems) { index, item ->
            InfoType(
                title = item.title,
                description = item.description,
                index = index
            )
        }
    }
}

@Composable
fun InfoType(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    index: Int,
    viewModel: MainViewModel = MainViewModel()
) {
    // Implement viewModel logic here to save state
    // For now I will still use MutableState
    val bool = viewModel.getValue(index)
    val onClick = remember { mutableStateOf(bool) }

    ArrowRow(name = title, onClick = onClick)

    if (onClick.value) {
        Line()
        InfoDetail(description = description)
    }
    Line()
}

@Composable
fun InfoDetail(modifier: Modifier = Modifier, description: String) {
    Box(
        modifier = modifier.padding(5.dp)
    ) {
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview
@Composable
private fun InfoPreview() {
    TaskTrackerTheme {
        Surface {
            InfoView()
        }
    }
}