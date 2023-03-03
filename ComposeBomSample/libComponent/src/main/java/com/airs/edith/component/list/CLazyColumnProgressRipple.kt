package com.airs.edith.component.list

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airs.edith.component.R
import com.airs.edith.component.extension.drawVerticalScrollbar
import com.airs.edith.component.extension.noRippleClickable

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CLazyColumnProgressRipple(
    map: Map<String, String>,
    clickEvent: (index: Int) -> Unit,
    backgroundColor: Color,
    foregroundColor: Color,
    height: Dp
) {
    val state = rememberLazyListState()
    LazyColumn(
        modifier = Modifier
            .drawVerticalScrollbar(state),
        state = state
    ) {
        itemsIndexed(map.keys.toList()) { index , key ->
            CLazyColumnProgressRippleAdapter(
                index = index,
                title = key,
                subTitle = map[key] ?: "",
                clickEvent = { clickEvent.invoke(index) },
                backgroundColor = backgroundColor,
                foregroundColor = foregroundColor,
                height = height
            )
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun CLazyColumnProgressRippleAdapter(
    index: Int,
    title: String,
    subTitle: String,
    clickEvent: () -> Unit,
    backgroundColor: Color,
    foregroundColor: Color,
    height: Dp
) {
    var selected by remember {
        mutableStateOf(false)
    }
    var progress by remember { mutableStateOf(0f) }

    val progressAnimDuration = 500
    val progressAnimation by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(
            durationMillis = progressAnimDuration, easing = LinearOutSlowInEasing
        ),
        finishedListener = {
            selected = false
            progress = 0f
        }
    )

    Box(
        modifier = Modifier
            .background(backgroundColor)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp) // card 와 card 사이 padding
                .noRippleClickable {
                    selected = true
                    progress = 1.0f
                    clickEvent.invoke()
                },
            elevation = CardDefaults.outlinedCardElevation(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(modifier = Modifier.padding(
                start = 5.dp,
                top = 5.dp,
                bottom = 5.dp
            ), //card 안에서의 row padding
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp) //row 내의 뷰들을 space 조절
            ) {
                Box(
                    modifier = Modifier
                        .size(
                            width = 28.dp,
                            height = 28.dp
                        )
                        .clip(CircleShape)
                        .border(2.dp, Color.Transparent, CircleShape)
                        .background(Color(R.color.icon_blue))
                ) {
                    Text(
                        text = "${index + 1}",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1, //maxLines 매개변수
                        overflow = TextOverflow.Ellipsis //텍스트 오버플로
                    )
                    Text(
                        text = subTitle,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 10.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        if (selected) {
            LinearProgressIndicator(
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .height(height)
                    .alpha(if (selected) 0.5f else 0f),
                trackColor = Color.Transparent,
                color = if (selected) foregroundColor else Color.Transparent , //progress color,
                progress = progressAnimation
            )
        }
    }
}

@Preview
@Composable
fun PreviewCLazyColumnProgressRipple() {
    CLazyColumnProgressRipple(
        map = getMap(),
        clickEvent = {},
        backgroundColor = Color.Transparent,
        foregroundColor = Color.LightGray,
        height = 45.dp)
}
