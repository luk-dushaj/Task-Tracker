package com.school.tasktracker.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.school.tasktracker.data.MainViewModel

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