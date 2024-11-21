package com.school.tasktracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.school.tasktracker.ui.theme.TaskTrackerTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.school.tasktracker.data.MainViewModel
import com.school.tasktracker.data.Routes
import com.school.tasktracker.data.Task
import com.school.tasktracker.views.AddTaskView
import com.school.tasktracker.views.DetailView
import com.school.tasktracker.views.EditView
import com.school.tasktracker.views.HomeView
import com.school.tasktracker.views.InfoView
import com.school.tasktracker.views.SettingsView
import com.school.tasktracker.views.HomeView
import com.school.tasktracker.views.SelectionView

// Project so far has only been tested on emulator Medium Phone API 35
// Should test on different emulators soon

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskTrackerTheme {
                MainView()
            }
        }
    }
}

@Composable
fun MainView(modifier: Modifier = Modifier) {
    val viewModel = MainViewModel()
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            TopBar(modifier = modifier, viewModel = viewModel, navController = navController)
        },
        bottomBar = {
            BottomBar(viewModel = viewModel, navController = navController)
        }
    ) { innerPadding ->
        // Main content area, using innerPadding to avoid overlap with the bottom bar
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // View content
            NavHost(navController = navController, startDestination = Routes.home) {
                composable(Routes.home) {
                    HomeView(
                        viewModel = viewModel,
                        navController = navController
                    )
                }
                composable(Routes.settings) { SettingsView(viewModel = viewModel) }
                composable(Routes.info) { InfoView() }
                composable(Routes.add) {
                    AddTaskView(
                        viewModel = viewModel,
                        navController = navController
                    )
                }
                composable(Routes.detail) {
                    DetailView(
                        viewModel = viewModel,
                        navController = navController
                    )
                }
                composable(Routes.selection) {
                    SelectionView(
                        viewModel = viewModel,
                        navController = navController
                    )
                }
                composable(Routes.edit) {
                    AddTaskView(
                        viewModel = viewModel,
                        navController = navController,

                    )
                }
            }
        }
    }
}

@Composable
fun TopBar(modifier: Modifier = Modifier, viewModel: MainViewModel, navController: NavController) {
    val viewNumber = viewModel.viewNumber.observeAsState().value ?: 0
    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = "Task Tracker",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium
        )
        // If Home is currently selected show Edit button
        if (viewNumber == 0 && !viewModel.isTasksEmpty()) {
            var isSelectionActive by remember { mutableStateOf(false) }
            Button(
                onClick = {
                    if (!viewModel.isTasksEmpty() && !isSelectionActive) {
                        isSelectionActive = true
                        navController.navigate(Routes.selection)
                    } else if (isSelectionActive) {
                        isSelectionActive = false
                        navController.navigate(Routes.home)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray
                )
            ) {
                Text(
                    text = if (isSelectionActive) "Done" else "Edit"
                )
            }

        }
    }
}

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    navController: NavController
) {
    val viewNumber by viewModel.viewNumber.observeAsState(initial = 0)
    var selectedItemIndex by remember { mutableIntStateOf(0) }

    val items = listOf("Home", "Settings", "Info", "Add")
    val selectedIcons = listOf(Filled.Home, Filled.Settings, Filled.Info, Filled.AddCircle)
    val unselectedIcons = listOf(
        Icons.Outlined.Home,
        Icons.Outlined.Settings,
        Icons.Outlined.Info,
        Icons.Outlined.Add
    )

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(

                icon = {
                    Icon(
                        if (viewNumber == index) selectedIcons[index] else unselectedIcons[index],
                        contentDescription = item
                    )
                },

                label = { Text(item) },
                selected = viewNumber == index,
                onClick = {
                    viewModel.updateViewNumber(index)
                    selectedItemIndex = viewNumber
                    when (viewNumber) {
                        0 -> navController.navigate(Routes.home)
                        1 -> navController.navigate(Routes.settings)
                        2 -> navController.navigate(Routes.info)
                        3 -> navController.navigate(Routes.add)
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainActivity() {
    TaskTrackerTheme {
        Surface {
            MainView()
        }
    }
}