package com.school.tasktracker.views

import android.annotation.SuppressLint
import android.app.ActivityManager.TaskDescription
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
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
import java.util.Date
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.MutableState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import java.util.Calendar
import java.util.*
import java.text.SimpleDateFormat
import com.school.tasktracker.components.HalfStarIcon
import com.school.tasktracker.data.Routes

// Logic will be implemented later

@SuppressLint("DefaultLocale")
@Composable
fun AddTaskView(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    editable: Boolean = false,
    navController: NavController,
    title: String = "",
    description: String = "",
    isPriority: Boolean = false,
    date: String = "",
    time: String = ""
) {
    val title = remember { mutableStateOf(title) }
    val description = remember { mutableStateOf(description) }
    val isPriority = remember { mutableStateOf(isPriority) }
    Column(
        modifier = modifier.padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        TaskTitle(title = title, isPriority = isPriority)
        TextRow("Date and time to complete by")
        DateTimeInput()
        TextRow("Task Description")
        ScrollableTextField(text = description)
        SaveRow(navController = navController)
    }
}

@Composable
fun DateTimeInput() {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // Set date and time format with AM/PM
    val dateFormat = SimpleDateFormat("MM-dd-yyyy", Locale.getDefault())

    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

    // Mutable state for the date and time input, initialized with current date and time
    var date by remember { mutableStateOf(dateFormat.format(calendar.time)) }
    var time by remember { mutableStateOf(timeFormat.format(calendar.time)) }

    val datePickerDialog = DatePickerDialog(
        context,
        { _, month, dayOfMoth, year ->
            calendar.set(month, dayOfMoth, year)
            date = dateFormat.format(calendar.time)
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
            time = timeFormat.format(calendar.time)
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
                text = date
            )
            Button(onClick = { datePickerDialog.show() }) {
                Text("Select Date")
            }
        }

        Column {
            Text(
                modifier = Modifier
                    .offset(x = 24.dp),
                text = time
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
fun SaveRow(modifier: Modifier = Modifier, navController: NavController) {
    Spacer(modifier = modifier.height(5.dp))
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
                containerColor = Color(0xFF007F00)
            ),
            onClick = {navController.popBackStack()}
        ) {
            Text("Save")
        }
    }
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