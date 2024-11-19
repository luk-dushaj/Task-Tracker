package com.school.tasktracker.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.school.tasktracker.components.TextRow
import com.school.tasktracker.data.MainViewModel
import com.school.tasktracker.data.Task
import com.school.tasktracker.ui.theme.TaskTrackerTheme
import java.util.UUID
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.school.tasktracker.data.Routes
import java.util.Date

@Composable
fun DetailView(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: MainViewModel
) {
    val task = viewModel.selectedTask
    if (task != null) {
        Column(
            modifier = modifier
                .background(
                    color = if (task.isPriority) Color(
                        red = 245,
                        green = 104,
                        blue = 115
                    ) else Color(red = 115, green = 152, blue = 245)
                ),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(
                modifier = modifier
                    .height(150.dp)
            )
            TextRow(
                title = task.title,
                color = Color.White,
                style = MaterialTheme.typography.displayMedium,
                weight = FontWeight.Bold
            )
            Text(
                text = task.description,
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall
            )
            DateTextComposable(
                text = buildAnnotatedString {
                    append("Due by: ")
                    dataText(task.date)
                }
            )
            DateTextComposable(
                text = buildAnnotatedString {
                    append("At: ")
                    dataText(task.time)
                }
            )
            DateTextComposable(
                text = buildAnnotatedString {
                    append("There is ")
                    dataText("${task.days}")
                    append(" days remaining")
                }
            )
            ExitButton(navController = navController)
        }
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TextRow(
                title = "Task not found",
                style = MaterialTheme.typography.headlineSmall,
                weight = FontWeight.Bold
            )
            Text(
                text = "Please exit screen or reset the app/the device itself.",
                color = Color.Red,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

// function to take in the custom string and organize normal text
@Composable
fun DateTextComposable(
    modifier: Modifier = Modifier,
    // This so I can customize my formatting to differentiate data to the user
    text: AnnotatedString
) {
    Text(
        text = text,
        color = Color.White,
        style = MaterialTheme.typography.headlineSmall,
    )
}

// Custom text function for important values like data.time
fun AnnotatedString.Builder.dataText(
    text: String,
) {
    withStyle(
        style = SpanStyle(
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textDecoration = TextDecoration.Underline
        )
    ) {
        append(text)
    }
}

@Composable
fun ExitButton(modifier: Modifier = Modifier, navController: NavController) {
    Button(
        onClick = {
            navController.navigate(Routes.home)
        },
        colors = ButtonDefaults.buttonColors(
            // Black so it can standout against both priority colors
            containerColor = Color.Black
        )
    ) {
        Text(
            text = "Exit",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewDetailView() {
    val task = Task(
        title = "Do laundry",
        description = "Go to laundromat, set timer for 30 minutes, come home.",
        isPriority = false,
    )

    val viewModel = MainViewModel()

    viewModel.addTask(task)

    viewModel.selectedTask = task

    TaskTrackerTheme {
        Surface {
            DetailView(viewModel = viewModel, navController = rememberNavController())
        }
    }

}