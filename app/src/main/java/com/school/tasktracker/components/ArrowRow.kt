package com.school.tasktracker.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
        TextRow(
            modifier = modifier
            // Align the text with the arrow
            .offset(y = 5.dp),
            title = name
        )
        ArrowIcon(flip = onClick.value)
    }
}