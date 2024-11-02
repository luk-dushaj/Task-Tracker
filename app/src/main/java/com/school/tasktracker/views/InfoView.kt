package com.school.tasktracker.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.school.tasktracker.data.InfoItem
import com.school.tasktracker.data.MainViewModel
import com.school.tasktracker.components.*

@Composable
fun InfoView(modifier: Modifier = Modifier, viewModel: MainViewModel) {

    var infoItems = listOf(
        InfoItem(
            title = "How to Add a Task",
            description = """
                To add a task, click the '+' button located at the bottom-right corner of the Home screen.
            """.trimIndent(),
        ),
        InfoItem(
            title = "How to Edit a Task",
            description = """
                First, click the 'Edit' button on the top-left of the Home screen. 
                This will make a pencil icon appear next to each task. 
                Tap the pencil icon of the task you want to edit, and a new screen will open where you can change the task details.
            """.trimIndent(),
        ),
        InfoItem(
            title = "How to Import and Export Tasks",
            description = """
                Go to the Settings screen by clicking the settings icon in the navigation bar. 
                To export your tasks, click the 'Export' button to download your tasks as a JSON file.
                To import tasks, click the 'Import' button, and upload a JSON file with your tasks.
            """.trimIndent(),
        ),
        InfoItem(
            title = "How to Change the App Theme",
            description = """
                In the Settings screen, you will see a toggle for Light and Dark mode.
                Tap the toggle to switch between themes.
            """.trimIndent(),
        )
    )
    LazyColumn {
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
fun InfoType(modifier: Modifier = Modifier, title: String, description: String, index: Int, viewModel: MainViewModel = MainViewModel()) {
    val bool = viewModel.getValue(index)
    val isClicked = remember { mutableStateOf(bool) }
    ArrowRow(name = title, onClick = isClicked)
    if (isClicked.value) {
        viewModel.toggleValue(index)
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