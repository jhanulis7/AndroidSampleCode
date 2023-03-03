package com.airs.edith.component.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// on below line we are creating a function for custom progress bar.
@Composable
fun ProgressBar(
    modifier: Modifier = Modifier,
    colors: List<Color> = listOf(
        Color(0xFF0F9D58),
        Color(0xF055CA4D)
    ),
    progress: Int = 75,
    with: Dp = 300.dp,
    height: Dp = 30.dp,
    shape: Shape = RoundedCornerShape(15.dp),
    fontSize: TextUnit = 15.sp,
    fontWeight: FontWeight = FontWeight.Bold,
    fontColor: Color = Color.White
) {
    // in this method we are creating a column
    Column(
        // in this column we are specifying modifier to
        // align the content within the column
        // to center of the screen.
        modifier = modifier,

        // on below line we are specifying horizontal
        // and vertical alignment for the content of our column
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // in this column we are creating a variable
        // for the progress of our progress bar.
        // on the below line we are creating a box.
        Box(
            // inside this box we are adding a modifier
            // to add rounded clip for our box with
            // rounded radius at 15
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp))
                // on below line we are specifying
                // height for the box
                .height(height)

                // on below line we are specifying
                // background color for box.
                .background(Color.Gray)

                // on below line we are
                // specifying width for the box.
                .width(with)
        ) {
            // in this box we are creating one more box.
            Box(
                // on below line we are adding modifier to this box.
                modifier = modifier
                    // on below line we are adding clip \
                    // for the modifier with round radius as 15 dp.
                    .clip(shape)

                    // on below line we are
                    // specifying height as 30 dp
                    .height(height)

                    // on below line we are adding background
                    // color for our box as brush
                    .background(
                        // on below line we are adding brush for background color.
                        Brush.horizontalGradient(colors)
                    )
                    // on below line we are specifying width for the inner box
                    .width(with * progress / 100)
            )
            // on below line we are creating a text for our box
            Text(
                // in text we are displaying a text as progress bar value.
                text = "$progress %",

                // on below line we are adding
                // a modifier to it as center.
                modifier = Modifier.align(Alignment.Center),

                // on below line we are adding
                // font size to it.
                fontSize = fontSize,

                // on below line we are adding
                // font weight as bold.
                fontWeight = fontWeight,

                // on below line we are
                // specifying color for our text
                color = fontColor
            )
        }
    }
}

@Preview
@Composable
fun Preview_ProgressBar() {
    ProgressBar(
        progress = 50
    )
}