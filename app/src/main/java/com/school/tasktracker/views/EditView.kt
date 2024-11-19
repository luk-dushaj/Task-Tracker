package com.school.tasktracker.views

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.school.tasktracker.components.PriorityComposable
import com.school.tasktracker.data.MainViewModel

@Composable
fun EditView(modifier: Modifier = Modifier, viewModel: MainViewModel, navController: NavController) {
    Column {
        PriorityComposable(isPriority = true, viewModel = viewModel, navController = navController)
        PriorityComposable(isPriority = false, viewModel = viewModel, navController = navController)
    }
}