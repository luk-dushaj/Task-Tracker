package com.school.tasktracker.views

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.school.tasktracker.components.HalfStarIcon
import com.school.tasktracker.components.TextRow
import com.school.tasktracker.data.MainViewModel
import com.school.tasktracker.data.Routes
import com.school.tasktracker.data.Task
import com.school.tasktracker.ui.theme.TaskTrackerTheme
import com.school.tasktracker.ui.theme.evergreen
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

// Logic will be implemented later

// This view will also be used as a view to edit a existing Task's properties

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddTaskView(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    editable: Boolean = false,
    navController: NavController,
) {
    var title: String
    var description: String
    var isPriority: Boolean
    var date: String
    var time: String

    DisposableEffect(Unit) {
        onDispose {
            viewModel.changeEditButton(false)
            title = ""
            description = ""
            isPriority = false
            date = LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))
            time = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"))
        }
    }

    // Prefill values if editing an existing task
    if (viewModel.selectedTask != null) {
        val task = viewModel.selectedTask!!
        title = task.title
        description = task.description
        isPriority = task.isPriority
        date = task.date.ifBlank { LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")) }
        time = task.time.ifBlank { LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a")) }
    } else {
        title = ""
        description = ""
        isPriority = false
        date = LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))
        time = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"))
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
        TaskTitle(title = titleState, isPriority = isPriorityState, viewModel = viewModel)
        TextRow("Date and time to complete by", color = viewModel.getColor())
        DateTimeInput(date = dateState, time = timeState)
        TextRow("Task Description", color = viewModel.getColor())
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

    val dateFormat = SimpleDateFormat("MM-dd-yyyy", Locale.getDefault())
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            date.value = dateFormat.format(calendar.time)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hour, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            time.value = timeFormat.format(calendar.time)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        false
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = date.value.ifEmpty { "Select Date" })
            Button(onClick = { datePickerDialog.show() }) {
                Text("Select Date")
            }
        }

        Column {
            Text(text = time.value.ifEmpty { "Select Time" })
            Button(onClick = { timePickerDialog.show() }) {
                Text("Select Time")
            }
        }
    }
}

@Composable
fun TaskTitle(title: MutableState<String>, isPriority: MutableState<Boolean>, viewModel: MainViewModel) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextRow("Task Title", color = viewModel.getColor())
            PriorityRow(bool = isPriority, viewModel = viewModel)
        }
        TextField(
            value = title.value,
            onValueChange = { title.value = it },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun PriorityRow(bool: MutableState<Boolean>, viewModel: MainViewModel) {
    Row(
        modifier = Modifier.clickable { bool.value = !bool.value }
    ) {
        TextRow("Priority", color = viewModel.getColor())
        Spacer(modifier = Modifier.width(4.dp))
        HalfStarIcon(filled = bool.value)
    }
}

@Composable
fun ScrollableTextField(text: MutableState<String>) {
    TextField(
        value = text.value,
        onValueChange = { text.value = it },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .verticalScroll(rememberScrollState()),
        maxLines = Int.MAX_VALUE
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SaveRow(
    viewModel: MainViewModel,
    navController: NavController,
    titleValue: String,
    descriptionValue: String,
    isPriorityValue: Boolean,
    dateValue: String,
    timeValue: String
) {
    var error by remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf("") }

    if (error) {
        Text(text = errorMessage.value, color = Color.Red)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red
            ),
            onClick = {
                viewModel.updateViewNumber(0)
                navController.navigate(Routes.home)
            }) {
            Text("Cancel")
        }
        Spacer(modifier = Modifier.width(20.dp))
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = evergreen
            ),
            onClick = {
            error = errorChecking(
                title = titleValue,
                description = descriptionValue,
                date = dateValue,
                time = timeValue,
                errorMessage = errorMessage
            )
            if (!error) {
                if (viewModel.selectedTask == null) {
                    viewModel.addTask(
                        Task(title = titleValue, description = descriptionValue, isPriority = isPriorityValue, date = dateValue, time = timeValue)
                    )
                } else {
                    val id = viewModel.selectedTask!!.id
                    viewModel.updateTaskById(id, titleValue, descriptionValue, isPriorityValue, dateValue, timeValue)
                }
                viewModel.changeEditButton(false)
                viewModel.updateViewNumber(0)
                viewModel.saveTasksToFile()
                navController.navigate(Routes.home)
            }
        }) {
            Text("Save")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun errorChecking(
    title: String,
    description: String,
    date: String,
    time: String,
    errorMessage: MutableState<String>
): Boolean {
    if (title.isBlank() || description.isBlank() || date.isBlank() || time.isBlank()) {
        errorMessage.value = "All fields must be filled."
        return true
    }

    val dateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault())

    return try {
        val parsedDate = LocalDate.parse(date, dateFormatter)
        val parsedTime = LocalTime.parse(time, timeFormatter)
        if (parsedDate.isBefore(LocalDate.now()) || (parsedDate == LocalDate.now() && parsedTime.isBefore(LocalTime.now()))) {
            errorMessage.value = "Deadline cannot be in the past."
            true
        } else false
    } catch (e: Exception) {
        errorMessage.value = "Invalid date or time format."
        true
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun AddTaskPreview() {
    TaskTrackerTheme {
        Surface {
            AddTaskView(viewModel = MainViewModel(
                application = TODO()
            ), navController = rememberNavController())
        }
    }
}