package com.school.tasktracker
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.school.tasktracker.ui.theme.TaskTrackerTheme

// Project so far has only been tested on emulator Medium Phone API 35
// Should test on different emulators soon

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskTrackerTheme {
                val viewModel = MainViewModel()
                MainView(viewModel = viewModel)
            }
        }
    }
}


// ViewModel is going to be used for sharing data across views in a unified way
// And for storing important data in general like Task()

// There will be a lot of example data for now because I am focusing on the UI currently
class MainViewModel: ViewModel() {
    // MutableLiveData to hold the counter value

    // Some example code to test functionality of edit button
    private val _counter = MutableLiveData(0)
    val counter: LiveData<Int> = _counter

    fun increment() {
        // ?: is the Elvis operator, means if the value preceding ?: is null then insert the change the value to the right of ?:
        // https://stackoverflow.com/questions/48253107/what-does-do-in-kotlin-elvis-operator
        _counter.value = (_counter.value ?: 0) + 1
    }
}

@Composable
// Like Top priority, Low priority
fun TodoType(modifier: Modifier, text: String, textColor: Color, boxColor: Color) {
    Column(
        modifier = modifier.offset(y = 35.dp)
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(color = boxColor)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.headlineSmall,
                color = textColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun TodoView(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    // Separate between priority lists
    val distance = modifier.offset(y = 165.dp)
    Column {
        // Have to wrap priorites text in a box
        // So I can easily dyanmically center it
        Box (
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Priorities",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
        }
        TodoType(
            modifier = modifier,
            text = "High",
            textColor = Color.Black,
            boxColor = Color.Gray
        )
        TodoType(
            modifier = distance,
            text = "Low",
            textColor = Color.Black,
            boxColor = Color.Gray
        )
    }
}

@Composable
fun TopBar(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    Row (
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
        Button(
            onClick = {
                viewModel.increment()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.DarkGray
            )
        ) {
            Text("Edit")
        }
    }
}

@Composable
fun BottomBar(modifier: Modifier = Modifier) {
    // State for the selected item in the navigation bar
    var selectedItemIndex by remember { mutableStateOf(0) }

    // Define the items and their corresponding icons
    val items = listOf("Home", "Settings", "Info", "Add")
    val selectedIcons = listOf(Filled.Home, Filled.Settings, Filled.Info, Filled.AddCircle)
    val unselectedIcons = listOf(Icons.Outlined.Home, Icons.Outlined.Settings, Icons.Outlined.Info, Icons.Outlined.Add)

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(

                icon = {
                    Icon(
                        if (selectedItemIndex == index) selectedIcons[index] else unselectedIcons[index],
                        contentDescription = item
                    )
                },

                label = { Text(item) },
                selected = selectedItemIndex == index,
                onClick = { selectedItemIndex = index }
            )
        }
    }
}

@Composable
fun InfoDetail(modifier: Modifier = Modifier, description: String) {
    Box(
        modifier = modifier.padding(5.dp)
    ) {
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun ArrowIcon(modifier: Modifier = Modifier, flip: Boolean) {
    if (!flip) {
        Icon(
            modifier = modifier
                .size(50.dp),
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "RightArrow"
        )
    } else {
        // When InfoType is clicked simulate turn animation
        Icon(
            modifier = modifier
                .size(50.dp),
            imageVector = Filled.KeyboardArrowDown,
            contentDescription = "DownArrow"
        )
    }
}

@Composable
fun InfoType(modifier: Modifier = Modifier, title: String, description: String) {
    var isDetail = remember { mutableStateOf(false) }
    Row(
        modifier = modifier
            .padding()
            .fillMaxWidth()
            .statusBarsPadding()
            .clickable { isDetail.value = !isDetail.value },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = modifier
                // Align the text with the arrow
                .offset(y = 5.dp),
            text = title,
            style = MaterialTheme.typography.headlineSmall
        )
        ArrowIcon(flip = isDetail.value)
    }
    if (isDetail.value) {
        HorizontalDivider(
            modifier = modifier,
            thickness = 3.dp,
            color = Color.Black
        )
        InfoDetail(description = description)
    }
    HorizontalDivider(
        thickness = 3.dp,
        color = Color.Black
    )
}

@Composable
fun InfoView(modifier: Modifier = Modifier) {
    Column {
        InfoType(
            title = "How to Add a Task",
            description = """
                To add a task, click the '+' button located at the bottom-right corner of the Home screen.
            """.trimIndent()
        )
        InfoType(
            title = "How to Edit a Task",
            description = """
                First, click the 'Edit' button on the top-left of the Home screen. 
                This will make a pencil icon appear next to each task. 
                Tap the pencil icon of the task you want to edit, and a new screen will open where you can change the task details.
            """.trimIndent()
        )
        InfoType(
            title = "How to Import and Export Tasks",
            description = """
                Go to the Settings screen by clicking the settings icon in the navigation bar. 
                To export your tasks, click the 'Export' button to download your tasks as a JSON file.
                To import tasks, click the 'Import' button, and upload a JSON file with your tasks.
            """.trimIndent()
        )
        InfoType(
            title = "How to Change the App Theme",
            description = """
                In the Settings screen, you will see a toggle for Light and Dark mode.
                Tap the toggle to switch between themes.
            """.trimIndent()
        )
    }
}


@Composable
fun MainView(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    val value by viewModel.counter.observeAsState()
    Scaffold(
        topBar = {
            TopBar(modifier = modifier, viewModel = viewModel)
        },
        bottomBar = {
            BottomBar()
        }
    ) { innerPadding ->
        // Main content area, using innerPadding to avoid overlap with the bottom bar
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column (
                modifier = modifier
                    .padding(5.dp)
            ) {
                // View content
                //TodoView(modifier = modifier, viewModel = viewModel)
                InfoView()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainActivity() {
    TaskTrackerTheme {
        Surface {
            val viewModel = MainViewModel()
            MainView(viewModel = viewModel)
        }
    }
}