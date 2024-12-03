package com.school.tasktracker.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.school.tasktracker.components.TextRow
import com.school.tasktracker.data.MainViewModel
import com.school.tasktracker.data.Routes
import com.school.tasktracker.ui.theme.lightBlue
import com.school.tasktracker.ui.theme.lightRed

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailView(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: MainViewModel
) {
    val task = viewModel.selectedTask
    if (task != null) {
        val daysLeft = viewModel.getDays(task.date)
        Column(
            modifier = modifier
                .padding(15.dp)
                .background(
                    color = if (task.isPriority) lightRed else lightBlue
                ),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(
                modifier = modifier
                    .height(50.dp)
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
                    dataText("$daysLeft")
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