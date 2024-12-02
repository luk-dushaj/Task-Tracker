package com.school.tasktracker.views

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.compose.rememberNavController
import com.school.tasktracker.components.TextRow
import com.school.tasktracker.data.MainViewModel
import com.school.tasktracker.data.Task
import com.school.tasktracker.ui.theme.TaskTrackerTheme
import com.school.tasktracker.components.HalfStarIcon
import com.school.tasktracker.components.PriorityComposable
import com.school.tasktracker.components.TaskContent
import com.school.tasktracker.data.Routes

@Composable
fun HomeView(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    navController: NavController
) {
    // Observe tasks and track if empty
    val tasks by viewModel.tasks.observeAsState(emptyList())
    val isTasksEmpty = tasks.isEmpty()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(25.dp)
    ) {
        // Have to wrap priorities text in a box
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isTasksEmpty) "There are currently no tasks" else "Tasks",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
        }

        // Fill out the UI a bit more
        Spacer(modifier = modifier.height(10.dp))

        if (!isTasksEmpty) {
            // Show task content if tasks are not empty
            TaskContent(
                viewModel = viewModel,
                navController = navController,
                onClick = {
                    navController.navigate(Routes.detail)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeView() {
    TaskTrackerTheme {
        Surface {
            HomeView(viewModel = viewModel(), navController = rememberNavController())
//            PriorityComposable(viewModel = viewModel(), isPriority = false)
//            ColoredLine(color = Color.Blue)
        }
    }
}