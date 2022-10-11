package com.my.samplecode.composeripplecomponent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.my.samplecode.composeripplecomponent.ui.theme.ComposeRippleComponentTheme


//reference : https://blog.canopas.com/jetpack-compose-cool-button-click-effects-c6bbecec7bcb
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeRippleComponentTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Column(
        modifier = Modifier.padding(30.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Click Effect Component")
        Spacer(modifier = Modifier.padding(top = 30.dp))
        BounceEffect("Bounce")
        Spacer(modifier = Modifier.padding(top = 20.dp))
        BounceEffect("Click")
        Spacer(modifier = Modifier.padding(top = 20.dp))
        ShakeEffect("Shake")
        Spacer(modifier = Modifier.padding(top = 20.dp))
        ShapeEffect("Shape")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeRippleComponentTheme {
        Greeting("Android")
    }
}

@Composable
fun PulsateEffect(name: String) {
    Button(
        onClick = {
        },
        contentPadding = PaddingValues(16.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.pressBounceEffect()
    ) {
        Text(text = "Click $name!")
    }
}

@Composable
fun BounceEffect(name: String) {
    Button(
        onClick = {
        },
        contentPadding = PaddingValues(16.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.pressBounceEffect()
    ) {
        Text(text = "Click $name!")
    }
}

@Composable
fun ShakeEffect(name: String) {
    Button(
        onClick = {
        },
        contentPadding = PaddingValues(16.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.pressShakeEffect()
    ) {
        Text(text = "Click $name!")
    }
}

@Composable
fun ShapeEffect(name: String) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState()
    val cornerRadius by animateDpAsState(targetValue = if (isPressed.value) 10.dp else 50.dp)

    Box(
        modifier = Modifier
            .background(color = Color.Blue, RoundedCornerShape(cornerRadius))
            .size(80.dp)
            .clip(RoundedCornerShape(cornerRadius))
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple()
            ) {
                //Clicked
            }
            .padding(horizontal = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Click\n$name",
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}