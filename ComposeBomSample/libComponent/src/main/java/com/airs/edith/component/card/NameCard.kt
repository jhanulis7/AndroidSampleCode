package com.airs.edith.component.card

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun NameCard(
    callback: () -> Unit,
    modifier: Modifier = Modifier,
    name: String = "",
    desc: String = ""
) {
    Row(
        modifier = modifier
            .clickable(onClick = {
                //do nothing
                callback.invoke()
            })
            .padding(end = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(modifier = Modifier.size(50.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)) {
            // 이미지 로딩 처리 부분
        }
        Column (
            modifier = Modifier.padding(start = 8.dp)
                .align(Alignment.CenterVertically)
        ) {
            Column {
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Preview
@Composable
fun Preview_NameCard() {
    val modifier = Modifier
        .wrapContentWidth()
        .padding(10.dp)
        .clip(RoundedCornerShape(30.dp))
        .background(MaterialTheme.colorScheme.surface)
    val callback: () -> Unit = {
        Log.d("TAG", "Click")
    }
    NameCard(
        modifier = modifier,
        callback = callback,
        name = "name",
        desc ="compose study"
    )
}