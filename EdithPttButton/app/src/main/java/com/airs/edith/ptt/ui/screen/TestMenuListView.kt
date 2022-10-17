package com.airs.edith.ptt.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airs.edith.ptt.R
import com.airs.edith.ptt.ui.manager.EdithTestManager

@Composable
fun TestMenuListView(
    testManager: EdithTestManager,
    modifier: Modifier = Modifier
) {
    val testMenuArray = stringArrayResource(id = R.array.test_menu_array)
    val testMenuDescArray = stringArrayResource(id = R.array.test_menu_desc_array)

    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) { // does not support scroll bar
        items(testMenuArray.toList()) { item ->
            TestMenuItem(testMenuArray.indexOf(item), text = item, description = testMenuDescArray[testMenuArray.indexOf(item)],testManager) }
    }
}

@Composable
fun TestMenuItem(
    index: Int,
    text: String,
    description: String,
    testManager: EdithTestManager,
) {
    Card(
        modifier = Modifier
            .padding(2.dp) // card 와 card 사이 padding
            .fillMaxWidth().clickable {
                testManager.runTestMenuList(index)
            },
        elevation  = 5.dp,
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
                    text = text,
                    style = MaterialTheme.typography.subtitle2,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1, //maxLines 매개변수
                    overflow = TextOverflow.Ellipsis //텍스트 오버플로
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.body2,
                    fontSize = 10.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

    }
}
