package com.example.navigationsample.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.navigation.NavHostController
import com.example.navigationsample.ui.navigate.NavRoutes

@Composable
fun Welcome(navController: NavHostController, userName: String?) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Welcome $userName", style = MaterialTheme.typography.titleMedium)
            Button(onClick = { navController.navigate(NavRoutes.Profile.routes) {
                popUpTo(NavRoutes.Home.routes)
            } }) {
                Text(text = "Set up your Profile")
            }
        }
    }
}