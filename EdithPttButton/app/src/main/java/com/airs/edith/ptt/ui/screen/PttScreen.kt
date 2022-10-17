package com.airs.edith.ptt.ui.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airs.edith.ptt.R
import com.airs.edith.ptt.ui.manager.EdithTestManager

@Composable
@Preview
fun PttScreen_Preview() {
    val context = LocalContext.current
    PttScreen(EdithTestManager(context))
}

@Composable
fun PttScreen(
    testManager: EdithTestManager
) {
    val modifier = Modifier
        .size(width = 200.dp, height = 480.dp)
        .background(Color.Transparent)
    val menuSettingsArray = stringArrayResource(id = R.array.vr_menu_array)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        PttButtonView(modifier = modifier)
        PttSettings(
            menuSettingsArray = menuSettingsArray,
            modifier = modifier
        )
        TestMenuListView(
            testManager,
            modifier = modifier
        )
    }
}

@Composable
fun PttButtonView(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.ic_speaking_end),
        modifier = Modifier
            .size(200.dp)
            .background(Color.Transparent)
            .clickable {
                Log.d("PttButtonView", "clickable")
            },
        contentScale = ContentScale.Crop,
        contentDescription = "PTT 버튼을 눌러주세요!" // decorative element
    )
}

@Composable
fun PttSettings(
    menuSettingsArray: Array<String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingValues(2.dp))
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            menuSettingsArray.forEach { menu ->
                val icon = when(menu) {
                    "초기화1" -> Icons.Rounded.ExitToApp
                    "초기화2" -> Icons.Rounded.ExitToApp
                    "초기화3" -> Icons.Rounded.ExitToApp
                    "초기화4" -> Icons.Rounded.ExitToApp
                    "초기화5" -> Icons.Rounded.ExitToApp
                    else -> Icons.Rounded.ExitToApp
                }
                Icon(
                    imageVector = icon,
                    modifier = Modifier.size(32.dp),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.padding(4.dp))
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            menuSettingsArray.forEach { menu ->
                Text(
                    text = menu,
                    style = (MaterialTheme.typography).body1,
                    color = Color.Yellow,
                    fontSize = 6.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .size(width = 32.dp, height = 10.dp)
                        .background(Color.Black)
                )
                Spacer(modifier = Modifier.padding(4.dp))
            }
        }
    }

}