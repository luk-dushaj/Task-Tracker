package com.school.tasktracker.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.school.tasktracker.data.MainViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskContent(modifier: Modifier = Modifier, viewModel: MainViewModel, navController: NavController, editable: Boolean = false, onClick: () -> Unit) {
    PriorityComposable(
        isPriority = true,
        viewModel = viewModel,
        navController = navController,
        editable = editable,
        onClick = onClick
    )
    PriorityComposable(
        isPriority = false,
        viewModel = viewModel,
        navController = navController,
        editable = editable,
        onClick = onClick
    )
}