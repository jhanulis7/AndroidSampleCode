package com.my.samplecode.composeviewmodel.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.my.samplecode.composeviewmodel.viewmodel.MyViewModel

//https://developer.android.com/training/dependency-injection/hilt-jetpack
@Composable
fun MyScreen(
    modifier: Modifier = Modifier,
    viewModel: MyViewModel = hiltViewModel()
) {
    Log.d("TEST", "MyScreen In - ReComposition")
    val state = viewModel.state.value

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Accessing state data here
            Text(text = "Counter: ${state.counter}")
            Text(text = "Button number: ${state.buttonNumber}")
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Emitting events to the viewModel, which internally mutates the data
            Button(onClick = {viewModel.onEvent(MyViewModel.UIEvent.IncrementCounter)}) {
                Text(text = "Increment counter")
            }
            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = {viewModel.onEvent(MyViewModel.UIEvent.ChooseButton(1))}) {
                    Text(text = "Button 1")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {viewModel.onEvent(MyViewModel.UIEvent.ChooseButton(2))}) {
                    Text(text = "Button 2")
                }
            }
        }
    }
}