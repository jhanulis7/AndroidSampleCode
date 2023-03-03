package com.airs.edith.component.plane

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Circle(
    height: Dp,
    color: Color,
    modifier: Modifier = Modifier,
    borderWidth: Dp = 0.dp,
    borderColor: Color = Color.White,
) {
    Box(
        modifier = modifier
            .size(height + borderWidth)
            .clip(CircleShape)
            .background(color)
            .border(borderWidth, borderColor, CircleShape)
    )
}

@Preview
@Composable
fun Preview_Circle() {
    Circle(
        height = 30.dp,
        color = Color.Blue
    )
}