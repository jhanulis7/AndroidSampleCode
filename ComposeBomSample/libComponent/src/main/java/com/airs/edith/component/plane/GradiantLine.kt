package com.airs.edith.component.plane

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GradiantLine(
    height: Dp,
    colors: List<Color>,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(50.dp)
) {
    Box(
        modifier = modifier
            .height(height)
            .clip(shape)
            .background(Brush.horizontalGradient(colors = colors))
    )
}

@Preview
@Composable
fun Preview_GradiantLine() {
    val modifier = Modifier.fillMaxWidth()
    GradiantLine(
        height = 30.dp,
        colors = listOf(
            Color(0xFF0F9D58),
            Color(0xF055CA4D)),
        modifier = modifier
    )
}