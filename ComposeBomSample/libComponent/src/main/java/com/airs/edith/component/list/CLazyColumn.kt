package com.airs.edith.component.list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airs.edith.component.R

@Composable
fun CLazyColumn(
    map: Map<String, String>,
    clickEvent: (index: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(map.keys.toList()) { index , key ->
            CLazyColumnAdapter(
                index = index,
                title = key,
                subTitle = map[key] ?: "",
                clickEvent = { clickEvent.invoke(index) }
            )
        }
    }
}

@Composable
fun CLazyColumnAdapter(
    index: Int,
    title: String,
    subTitle: String,
    clickEvent: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(2.dp) // card 와 card 사이 padding
            .fillMaxWidth()
            .clickable {
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
                    .size(width = 28.dp, height = 28.dp)
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
}

@Preview
@Composable
fun PreviewCLazyColumn() {
    CLazyColumn(map = getMap(), clickEvent = {})
}

fun getMap() : Map<String, String> {
    val map = mutableMapOf<String, String>()
    map["알고리즘1"] = "2개의 숫자를 선택해서 최대값을 구하세요."
    map["알고리즘2"] = "2개의 숫자를 선택해서 최대값을 구하세요."
    map["알고리즘3"] = "2개의 숫자를 선택해서 최대값을 구하세요."
    map["알고리즘4"] = "2개의 숫자를 선택해서 최대값을 구하세요."
    map["알고리즘5"] = "2개의 숫자를 선택해서 최대값을 구하세요."

    return map
}
