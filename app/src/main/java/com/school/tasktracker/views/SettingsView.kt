package com.school.tasktracker.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.school.tasktracker.data.MainViewModel
import com.school.tasktracker.components.ArrowRow
import com.school.tasktracker.components.TextRow

@Composable
fun SettingsView(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    val theme = viewModel.theme.observeAsState().value ?: "default"
    // To toggle theme dropdown menu
    var isClick = remember { mutableStateOf(false) }
    var position by remember { mutableStateOf(Offset.Zero) }
    Column (
        modifier = modifier.padding(5.dp)
    ) {
        TextRow(
            modifier = modifier.clickable {

            },
            title = "Import"
        )
        TextRow(
            modifier = modifier
                .offset(y = 15.dp)
                .clickable {

                },
            title = "Export"
        )
        ArrowRow(
            name = "Theme",
            onClick = isClick
        )

        DropdownMenu(
            expanded = isClick.value,
            onDismissRequest = { isClick.value = false },
            // Aligning it under the arrow icon
            offset = DpOffset(x = 275.dp, y = 0.dp)
        ) {
            DropdownMenuItem(
                text = { Text("Device Default") },
                onClick = { viewModel.updateTheme("default") }
            )
            DropdownMenuItem(
                text = { Text("Dark") },
                onClick = { viewModel.updateTheme("dark") }
            )
            DropdownMenuItem(
                text = { Text("Light") },
                onClick = { viewModel.updateTheme("light") }
            )
        }
    }
}