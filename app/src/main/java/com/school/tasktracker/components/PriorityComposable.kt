package com.school.tasktracker.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.school.tasktracker.data.MainViewModel
import com.school.tasktracker.ui.theme.darkBlue
import com.school.tasktracker.ui.theme.darkRed
import com.school.tasktracker.ui.theme.lightBlue
import com.school.tasktracker.ui.theme.lightRed
import java.util.UUID
import com.school.tasktracker.data.DateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
// This is a composable to hold all task priority objects.
// By default is used for HomeView with onClick opening a DetailView
// Editable is for the SelectionView and which leads to EditView
// onClick serves as a flexible way to reuse the same component in different ways:
// HomeView -> SelectionView -> EditView
fun PriorityComposable(
    modifier: Modifier = Modifier,
    isPriority: Boolean,
    viewModel: MainViewModel,
    navController: NavController,
    onClick: () -> Unit,
    editable: Boolean = false
) {
    val tasks by viewModel.tasks.observeAsState()
    val isTaskSelected = viewModel.isTaskSelectedInSelectionMode.observeAsState()
    val filteredTasks = tasks!!.filter { it.isPriority == isPriority }
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Row {
            TextRow(
                color = Color.Gray,
                weight = FontWeight.ExtraBold,
                title = if (isPriority) "High Priority" else "Low Priority"
            )
            Spacer(modifier = modifier.width(4.dp))
            if (isPriority) {
                HalfStarIcon(filled = true)
            }
        }
        ColoredLine(color = if (isPriority) Color.Red else Color.Blue)
    }
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(filteredTasks) { item ->
//            val daysLeft = DateTime().getDays(dueDate = item.date)
            TaskDesign(
                modifier = modifier
                    .clickable {
                        viewModel.selectedTask = item
                        onClick()
                    },
                title = item.title,
                days = 0,
                color = colorSelector(id = item.id, isPriority = isPriority, viewModel = viewModel, isTaskSelected = isTaskSelected),
                edit = editable
            )
        }
    }
}

@Composable
fun colorSelector(
    id: UUID,
    isPriority: Boolean,
    viewModel: MainViewModel,
    isTaskSelected: State<Boolean?>
): Color {
    return when {
        isTaskSelected.value == true && viewModel.isTaskSelected(id) -> {
            if (isPriority) darkRed else darkBlue
        }
        isTaskSelected.value == true && !viewModel.isTaskSelected(id) -> {
            if (isPriority) lightRed else lightBlue
        }
        else -> {
            if (isPriority) lightRed else lightBlue
        }
    }
}
