package com.school.tasktracker.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.school.tasktracker.components.ArrowRow
import com.school.tasktracker.components.Line
import com.school.tasktracker.data.InfoItem
import com.school.tasktracker.data.MainViewModel

val infoValue = mutableStateOf(List(6) { false })

private fun updateIndex(index: Int, newValue: Boolean) {
    val tempList = infoValue.value?.toMutableList()
    tempList?.set(index, newValue)
    infoValue.value = tempList!!
}

fun getValue(index: Int): Boolean {
    val value = infoValue.value.get(index)
    if (value) {
        return true
    }
    return false
}

@Composable
fun InfoView(modifier: Modifier = Modifier, viewModel: MainViewModel) {

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
                index = index,
                color = viewModel.getColor()
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
    color: Color,
) {
    // Implement viewModel logic here to save state
    // For now I will still use MutableState
    val bool = getValue(index)
    val onClick = remember { mutableStateOf(bool) }

    ArrowRow(name = title, onClick = onClick, color = color)

    if (onClick.value) {
        Line(color = color)
        InfoDetail(description = description)
    }
    Line(color = color)
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