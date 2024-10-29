package com.school.tasktracker
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
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

    private val _infoTypeCount = MutableLiveData(0)
    val infoTypeCount: LiveData<Int> = _infoTypeCount

    private val _infoType = MutableLiveData(mutableMapOf<Int, Boolean>())
    val infoType: LiveData<MutableMap<Int, Boolean>> = _infoType

    private val _dropDownOpen = MutableLiveData(false)
    private var bool = _dropDownOpen.value
    val dropDownOpen: LiveData<Boolean> = _dropDownOpen

    fun changeDropDown() {
        // ! opposite
        _dropDownOpen.value = !bool!! // !! to force unwrap the optional type
    }

    fun updateInfoTypeCount(bool: Boolean) {
        _infoTypeCount.value = _infoTypeCount.value?.plus(1)
        val index = _infoType.value?.minus(1)
        var map = _infoType.value.orEmpty().toMutableMap()

    }

    private val _viewNumber = MutableLiveData(0)
    val viewNumber: LiveData<Int> = _viewNumber

    // For device default color theme
    // So only viable themes are "default", "dark", "light"
    private val _theme = MutableLiveData("default")
    val theme : LiveData<String> = _theme

    fun updateViewNumber(newNumber: Int) {
        // ?: is the Elvis operator, means if the value preceding ?: is null then insert the change the value to the right of ?:
        // https://stackoverflow.com/questions/48253107/what-does-do-in-kotlin-elvis-operator
        _viewNumber.value = newNumber
    }

    fun updateTheme(themeName: String) {
        val themes = arrayOf("default", "dark", "light")

        if (themes.contains(themeName)) {
            _theme.value = themeName
        }
    }
}

@Composable
fun SetView(viewNumber: Int, viewModel: MainViewModel) {
    // Clearly viewNumber is equivalent to the selectedIndex from the BottomBar view
    // This function is to provide a more simpler and dynamic way to switch views based on active bottombar icon

    when (viewNumber) {
        // some values will have the same view for testing purposes
        0 -> TodoView(viewModel = viewModel)
        1 -> SettingsView(viewModel = viewModel)
        2 -> InfoView(viewModel = viewModel)
        3 -> InfoView(viewModel = viewModel)
        else -> Text("Unknown error, there was supposed to be a view generated.")
    }
}

@Composable
// Like Top priority, Low priority
fun TodoType(modifier: Modifier = Modifier, text: String, textColor: Color, boxColor: Color) {
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
        // This is where the Tasks will be placed
        // Eventually will make a Task composable
    }
}

@Composable
fun TodoView(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    // Separate between priority lists
    val distance = Modifier.offset(y = 165.dp)
    Column {
        // Have to wrap priorites text in a box
        // So I can easily dyanmically center it
        Box (
            modifier = Modifier.fillMaxWidth(),
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
            modifier = Modifier,
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
    val viewNumber = viewModel.viewNumber.observeAsState().value ?: 0
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
        // If Home is currently selected show Edit button
        if (viewNumber == 0) {
            Button(
                onClick = {
                    // Going to add edit view later
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray
                )
            ) {
                Text("Edit")
            }
        }
    }
}

@Composable
fun BottomBar(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    // State for the selected item in the navigation bar
    var selectedItemIndex by remember { mutableIntStateOf(0) }

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
                onClick = {
                    selectedItemIndex = index
                    viewModel.updateViewNumber(index)
                }
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
fun Line(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier,
        thickness = 3.dp,
        color = Color.Black
    )
}

@Composable
fun TextRow(modifier: Modifier = Modifier, title: String) {
    Text(
        modifier = modifier,
        text = title,
        style = MaterialTheme.typography.headlineSmall
    )
}

@Composable
// Custom row to make it easier for me to implement ArrowIcon
fun ArrowRow(modifier: Modifier = Modifier, name: String, onClick: MutableState<Boolean>) {
    Row (
    modifier = modifier
        .padding()
        .fillMaxWidth()
        .statusBarsPadding()
        .clickable { onClick.value = !onClick.value },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextRow(modifier = modifier
            // Align the text with the arrow
            .offset(y = 5.dp),
            title = name
        )
        ArrowIcon(flip = onClick.value)
    }
}

@Composable
fun InfoType(modifier: Modifier = Modifier, title: String, description: String) {
    val isClicked = remember { mutableStateOf(false) }
    ArrowRow(name = title, onClick = isClicked)
    if (isClicked.value) {
        Line()
        InfoDetail(description = description)
    }
    Line()
}

@Composable
fun InfoView(modifier: Modifier = Modifier, viewModel: MainViewModel) {
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


@Composable
fun MainView(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    // Need elvis operator here incase if value is null
    val viewNumber = viewModel.viewNumber.observeAsState().value ?: 0
    Scaffold(
        topBar = {
            TopBar(modifier = modifier, viewModel = viewModel)
        },
        bottomBar = {
            BottomBar(viewModel = viewModel)
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
                SetView(viewNumber = viewNumber, viewModel = viewModel)
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