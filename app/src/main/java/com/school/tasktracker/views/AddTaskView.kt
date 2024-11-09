package com.school.tasktracker.views

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.school.tasktracker.components.TextRow
import com.school.tasktracker.data.MainViewModel
import com.school.tasktracker.ui.theme.TaskTrackerTheme
import java.util.Date
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.MutableState
import androidx.compose.ui.draw.clip
import java.util.Calendar

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskView(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    var isManualClicked = remember { mutableStateOf(false) }
    var isDateClicked = remember { mutableStateOf(false) }
    fun onlyActivateOne() {
        if (isManualClicked.value) {
            isManualClicked.value = !isManualClicked.value
        }
    }

    var title by remember { mutableStateOf("") }
    var date: Date
    var description = remember { mutableStateOf("") }
    var isPriority = remember { mutableStateOf(false) }
    Column(
        modifier = modifier.padding(5.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextRow("Task Title")
            PriorityRow(bool = isPriority)
        }
        TextField(
            modifier = modifier.fillMaxWidth(),
            value = title,
            onValueChange = { newTitle -> title = newTitle }
        )
        TextRow("Date and time to complete by")
        /*Row {
            TextField(
                // Will implement time and date
            )
        }*/
        TextRow("Task Description")
        BigScrollableTextField(text = description)
    }
}

@Composable
fun PriorityRow(modifier: Modifier = Modifier, bool: MutableState<Boolean>) {
    Row (
        modifier = modifier
            .clickable {
                bool.value = !bool.value
            }
    ) {
        TextRow("Priority")
        if (bool.value) {
            HalfStarIcon(filled = true)
        } else {
            HalfStarIcon(filled = false)
        }
    }
}

@Composable
fun BigScrollableTextField(text: MutableState<String>) {
    // Scroll state for the text field to enable vertical scrolling
    val scrollState = rememberScrollState()

    TextField(
        value = text.value,
        onValueChange = { text.value = it },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .verticalScroll(scrollState),
        maxLines = Int.MAX_VALUE
    )
}

@Composable
fun HalfStarIcon(filled: Boolean) {
    val iconSize = 36.dp

    val outlineColor = if (filled) Color.Red else Color.Gray
    val halfFillColor = if (filled) Color.Red else Color.Gray
    val backgroundColor = if (filled) Color.White else Color.LightGray

    // Custom shape to clip only half of the star
    val halfShape = GenericShape { size, _ ->
        lineTo(size.width / 2, 0f)
        lineTo(size.width / 2, size.height)
        lineTo(0f, size.height)
        close()
    }

    Box(
        modifier = Modifier
            .size(iconSize)
            .offset( y = (-3).dp)
    ) {
        // Draw the outline (empty star) with the specified outline color
        Icon(
            imageVector = Icons.Outlined.Star,
            contentDescription = "Empty Star Outline",
            tint = outlineColor,
            modifier = Modifier.size(iconSize)
        )

        // Draw a filled background star with the specified background color
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = "White Background",
            tint = backgroundColor,
            modifier = Modifier.size(iconSize)
        )

        // Overlay with a clipped star to fill half of it with the specified half fill color
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = "Half Star",
            tint = halfFillColor,
            modifier = Modifier
                .size(iconSize)
                .clip(halfShape)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun AddTaskPreview() {
    TaskTrackerTheme {
        Surface {
            AddTaskView(viewModel = MainViewModel())
        }
    }
}