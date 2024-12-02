package com.school.tasktracker.views

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.school.tasktracker.components.TaskContent
import com.school.tasktracker.data.MainViewModel
import com.school.tasktracker.data.Routes

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SelectionView(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    navController: NavController
) {
    // Reset state when leaving the view
    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetSelectionState()
        }
    }

    val isSelectionMode by viewModel.isSelectionMode.observeAsState()

    Spacer(modifier = modifier.height(50.dp))
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(25.dp)
    ) {
        TaskContent(
            viewModel = viewModel,
            navController = navController,
            editable = if (isSelectionMode == true) false else true,
            onClick = {
                if (isSelectionMode == true) {
                    viewModel.selectedTask?.let { task ->
                        viewModel.addSelectedTask(task)
                        viewModel.toggleTaskSelectedInSelectionMode()
                    }
                } else {
                    viewModel.changeEditButton(bool = true)
                    navController.navigate(Routes.edit)
                }
            }
        )
    }
    ButtonRow(viewModel = viewModel)
}

@Composable
fun ButtonRow(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    val selectionMode by viewModel.isSelectionMode.observeAsState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.offset(y = 625.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            // Select Button
            Button(
                modifier = modifier.alpha(if (selectionMode == true) 0.5f else 1.0f),
                onClick = { viewModel.toggleSelectionMode() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
            ) {
                Text("Select")
            }

            // Delete Button
            Button(
                modifier = modifier.alpha(
                    if (viewModel.sizeOfSelectedTasks() > 0) 1.0f else 0.5f
                ),
                onClick = {
                    if (viewModel.sizeOfSelectedTasks() > 0) {
                        viewModel.deleteTasks()
                        viewModel.saveTasksToFile()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Delete")
            }

            // Select All Button
            Button(
                modifier = modifier.alpha(if (selectionMode == true) 1.0f else 0.5f),
                onClick = {
                    if (viewModel.isSelectedTasksFull()) {
                        viewModel.deSelectAllTasks()
                    } else {
                        viewModel.selectAllTasks()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
            ) {
                Text(if (viewModel.isSelectedTasksFull()) "Deselect All" else "Select All")
            }
        }
    }
}
