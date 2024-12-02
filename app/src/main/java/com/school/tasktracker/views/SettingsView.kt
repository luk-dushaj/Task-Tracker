package com.school.tasktracker.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.school.tasktracker.components.ArrowRow
import com.school.tasktracker.components.TextRow
import com.school.tasktracker.data.MainViewModel

@Composable
fun SettingsView(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    // To toggle theme dropdown menu
    val onClick = remember { mutableStateOf(false) }
    Column (
        modifier = modifier.padding(5.dp)
    ) {
        TextRow(
            modifier = modifier.clickable {
                viewModel.requestImport()
            },
            color = viewModel.getColor(),
            title = "Import"
        )
        TextRow(
            modifier = modifier
                .offset(y = 15.dp)
                .clickable {
                    viewModel.requestExport()
                    viewModel.saveTasksToFile()
                },
            color = viewModel.getColor(),
            title = "Export"
        )
        ArrowRow(
            name = "Theme",
            onClick = onClick,
            color = viewModel.getColor()
        )

        DropdownMenu(
            expanded = onClick.value,
            onDismissRequest = { onClick.value = false },
            // Aligning it under the arrow icon
            offset = DpOffset(x = 275.dp, y = 310.dp)
        ) {
            DropdownMenuItem(
                text = { Text("Device Default") },
                onClick = {
                    viewModel.updateTheme("default")
                }
            )
            DropdownMenuItem(
                text = { Text("Dark") },
                onClick = {
                    viewModel.updateTheme("dark")
                }
            )
            DropdownMenuItem(
                text = { Text("Light") },
                onClick = {
                    viewModel.updateTheme("light")
                }
            )
        }
    }
}