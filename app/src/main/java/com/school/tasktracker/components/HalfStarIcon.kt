package com.school.tasktracker.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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
            .offset(y = (-3).dp)
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