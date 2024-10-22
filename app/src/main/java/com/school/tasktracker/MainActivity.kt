package com.school.tasktracker
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.school.tasktracker.ui.theme.TaskTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskTrackerTheme {
                // State for the selected item in the navigation bar
                var selectedItem by remember { mutableStateOf(0) }

                // Define the items and their corresponding icons
                val items = listOf("Songs", "Artists", "Playlists")
                val selectedIcons = listOf(Filled.Home, Filled.Favorite, Filled.Star)
                val unselectedIcons = listOf(Icons.Outlined.Home, Icons.Outlined.FavoriteBorder, Icons.Outlined.Star)

                // Use Scaffold to create a layout with a bottom navigation bar
                Scaffold(
                    bottomBar = {
                        BottomBar()
                    }
                ) { innerPadding ->
                    // Main content area, using innerPadding to avoid overlap with the bottom bar
                    // You can replace this with your actual content
                    Surface(modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()) {
                        // Example content
                        Text("Main Content Area", modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun BottomBar(modifier: Modifier = Modifier) {
    // State for the selected item in the navigation bar
    var selectedItem by remember { mutableStateOf(0) }

    // Define the items and their corresponding icons
    val items = listOf("Home", "Settings", "Info", "Add")
    val selectedIcons = listOf(Filled.Home, Filled.Settings, Filled.Info, Filled.AddCircle)
    val unselectedIcons = listOf(Icons.Outlined.Home, Icons.Outlined.Settings, Icons.Outlined.Info, Icons.Outlined.Add)

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(

                icon = {
                    Icon(
                        if (selectedItem == index) selectedIcons[index] else unselectedIcons[index],
                        contentDescription = item
                    )
                },

                label = { Text(item) },
                selected = selectedItem == index,
                onClick = { selectedItem = index }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainActivity() {
    TaskTrackerTheme {
        MainActivity().setContent { }
    }
}