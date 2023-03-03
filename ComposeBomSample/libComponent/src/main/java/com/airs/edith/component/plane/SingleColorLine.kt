package com.airs.edith.component.plane

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SingleColorLine(
    height: Dp,
    color: Color,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(50.dp)
) {
    Box(
        modifier = modifier
            .clip(shape)
            .height(height)
            .background(color = color)
    )
}

@Preview
@Composable
fun Preview_SingleColorLine() {
    val modifier = Modifier.fillMaxWidth()
    SingleColorLine(
        modifier = modifier,
        height = 30.dp,
        color = Color.Blue
    )
}