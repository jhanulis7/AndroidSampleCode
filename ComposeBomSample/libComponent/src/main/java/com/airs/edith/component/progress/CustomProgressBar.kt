package com.airs.edith.component.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomProgressBar(
    modifier: Modifier,
    width: Dp,
    backgroundColor: Color,
    foregroundColor: Brush,
    percent: Int,
    isShownText: Boolean
) {
    Box(
        modifier = modifier
            .background(backgroundColor)
            .width(width)
    ) {
        Box(
            modifier = modifier
                .background(foregroundColor)
                .width(width * percent / 100)
        )
        if (isShownText) {
            Text(
                text = "$percent %",
                modifier = Modifier.align(alignment = Alignment.Center),
                fontSize = 12.sp
            )
        }
    }
}

@Preview
@Composable
fun Preview_CustomProgressBar() {
    CustomProgressBar(
        Modifier
            .clip(shape = RoundedCornerShape(15.dp))
            .height(14.dp),
        300.dp,
        Color.Gray,
        Brush.horizontalGradient(listOf(Color(0xffFD7D20), Color(0xffFBE41A))),
        65,
        true
    )
}