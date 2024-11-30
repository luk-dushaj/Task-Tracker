package com.school.tasktracker.views

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.school.tasktracker.components.TextRow
import com.school.tasktracker.data.MainViewModel
import com.school.tasktracker.ui.theme.TaskTrackerTheme
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import java.util.Calendar
import java.util.*
import java.text.SimpleDateFormat
import com.school.tasktracker.components.HalfStarIcon
import com.school.tasktracker.data.Routes
import com.school.tasktracker.data.Task
import com.school.tasktracker.ui.theme.evergreen

// Logic will be implemented later

// This view will also be used as a view to edit a existing Task's properties

@Composable
fun AddTaskView(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    editable: Boolean = false,
    navController: NavController,
) {
    val title: String
    val description: String
    val isPriority: Boolean
    val date: String
    val time: String

    DisposableEffect(Unit) {
        onDispose {
            viewModel.changeEditButton(false)
        }
    }

    // Means edit mode is on so prefill the value inputs
    if (viewModel.selectedTask != null) {
        val task = viewModel.selectedTask!!
        title = task.title
        description = task.description
        isPriority = task.isPriority
        date = task.date
        time = task.date
    // Just normal default values when creating a task
    } else {
        title = ""
        description = ""
        isPriority = false
        date = ""
        time = ""
    }
    val titleState = remember { mutableStateOf(title) }
    val descriptionState = remember { mutableStateOf(description) }
    val isPriorityState = remember { mutableStateOf(isPriority) }
    val dateState = remember { mutableStateOf(date) }
    val timeState = remember { mutableStateOf(time) }
    Column(
        modifier = modifier.padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        TaskTitle(title = titleState, isPriority = isPriorityState)
        TextRow("Date and time to complete by")
        DateTimeInput(date = dateState, time = timeState)
        TextRow("Task Description")
        ScrollableTextField(text = descriptionState)
        SaveRow(
            viewModel = viewModel,
            navController = navController,
            titleValue = titleState.value,
            descriptionValue = descriptionState.value,
            isPriorityValue = isPriorityState.value,
            dateValue = dateState.value,
            timeValue = timeState.value
        )
    }
}

@Composable
fun DateTimeInput(date: MutableState<String>, time: MutableState<String>) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // Set date and time format with AM/PM
    val dateFormat = SimpleDateFormat(date.value.ifBlank { "MM-dd-yyyy" }, Locale.getDefault())

    val timeFormat = SimpleDateFormat(time.value.ifBlank { "hh:mm a" }, Locale.getDefault())

    // Mutable state for the date and time input, initialized with current date and time
    val dateState by remember { mutableStateOf(dateFormat.format(calendar.time)) }
    val timeState by remember { mutableStateOf(timeFormat.format(calendar.time)) }

    val datePickerDialog = DatePickerDialog(
        context,
        { _, month, dayOfMoth, year ->
            calendar.set(month, dayOfMoth, year)
            date.value = dateFormat.format(calendar.time)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            time.value = timeFormat.format(calendar.time)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        false // false for 12-hour format with AM/PM
    )

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column {
            Text(
                modifier = Modifier
                    .offset(x = 12.dp),
                text = dateState
            )
            Button(onClick = { datePickerDialog.show() }) {
                Text("Select Date")
            }
        }

        Column {
            Text(
                modifier = Modifier
                    .offset(x = 24.dp),
                text = timeState
            )
            Button(onClick = { timePickerDialog.show() }) {
                Text("Select Time")
            }
        }
    }
}

@Composable
fun TaskTitle(
    title: MutableState<String>,
    isPriority: MutableState<Boolean>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextRow("Task Title")
        PriorityRow(bool = isPriority)
    }
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = title.value,
        onValueChange = { newTitle -> title.value = newTitle }
    )
}

@Composable
fun PriorityRow(modifier: Modifier = Modifier, bool: MutableState<Boolean>) {
    Row(
        modifier = modifier
            .clickable {
                bool.value = !bool.value
            }
    ) {
        TextRow("Priority")
        Spacer(modifier = modifier.width(4.dp))
        if (bool.value) {
            HalfStarIcon(filled = true)
        } else {
            HalfStarIcon(filled = false)
        }
    }
}

@Composable
fun ScrollableTextField(text: MutableState<String>) {
    val scrollState = rememberScrollState()

    TextField(
        value = text.value,
        onValueChange = { text.value = it },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .verticalScroll(scrollState),
        maxLines = Int.MAX_VALUE
    )
}

@Composable
fun SaveRow(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    navController: NavController,
    editable: Boolean = viewModel.selectedTask != null,
    titleValue: String,
    descriptionValue: String,
    isPriorityValue: Boolean,
    dateValue: String,
    timeValue: String
) {
    var error by remember { mutableStateOf(false) }
    Spacer(modifier = modifier.height(5.dp))
    if (error) {
        Text(
            text = "Make sure all the values are filled and the date is in the distant future"
        )
    }
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red
            ),
            onClick = {navController.navigate(Routes.home)}
        ) {
            Text("Cancel")
        }
        Spacer(modifier = modifier.width(20.dp))
        Button(
            colors = ButtonDefaults.buttonColors(
                // Darker shade of green, compared to the lime green default
                containerColor = evergreen
            ),
            onClick = {
                error = errorChecking(
                    title = titleValue,
                    description = descriptionValue,
                    date = dateValue,
                    time = timeValue
                )
                if (!error && !editable) {
                    viewModel.addTask(
                        Task (
                            title = titleValue,
                            description = descriptionValue,
                            isPriority = isPriorityValue,
                            date = dateValue,
                            time = timeValue
                        )
                    )
                    viewModel.updateViewNumber(0)
                    navController.navigate(Routes.home)
               } else { // If editable
                    val id = viewModel.selectedTask!!.id
                    viewModel.updateTaskById(
                        taskId = id,
                        title = titleValue,
                        description = descriptionValue,
                        isPriority = isPriorityValue,
                        date = dateValue,
                        time = titleValue
                    )
                    viewModel.selectedTask = null
                    // Turning it back on now
                    viewModel.toggleSelectionView()
                    navController.navigate(Routes.selection)
                }
                viewModel.changeEditButton(false)
            }
        ) {
            Text("Save")
        }
    }
}

fun errorChecking(
    title: String,
    description: String,
    date: String,
    time: String
): Boolean {
    if (title.isBlank() && description.isBlank() && date.isBlank() && time.isBlank()) {
        return true
    }
    // Eventually check for if time is greater than current time
    return false
}

@Preview(showBackground = true)
@Composable
fun AddTaskPreview() {
    TaskTrackerTheme {
        Surface {
            AddTaskView(viewModel = MainViewModel(), navController = rememberNavController())
        }
    }
}