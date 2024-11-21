package com.school.tasktracker.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.school.tasktracker.components.PriorityComposable
import com.school.tasktracker.components.TaskContent
import com.school.tasktracker.data.MainViewModel
import com.school.tasktracker.data.Routes
import com.school.tasktracker.data.Task
import com.school.tasktracker.ui.theme.TaskTrackerTheme

@Composable
fun SelectionView(modifier: Modifier = Modifier, viewModel: MainViewModel, navController: NavController) {
    Spacer(
        modifier = modifier.height(50.dp)
    )
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(25.dp)
    ) {
        TaskContent(
            viewModel = viewModel,
            navController = navController,
            editable = true,
            onClick = {
                navController.navigate(Routes.edit)
            })
    }
    ButtonRow()
}

@Composable
fun ButtonRow(modifier: Modifier = Modifier) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.offset(y = 625.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray
                )
            ) {
                Text("Select")
            }
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
                )
            ) {
                Text("Delete")
            }
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray
                )
            ) {
                Text("Select All")
            }
        }
    }
}

@Preview
@Composable
private fun PreviewSelectionView() {
    TaskTrackerTheme {
        Surface {
            val viewModel = MainViewModel()
            viewModel.addTask(
                Task (
                    title = "Go shopping",
                    description = "Get eggs, bread and fruits",
                    isPriority = true
                )
            )
            SelectionView(viewModel = viewModel, navController = rememberNavController())
        }
    }
}