package com.example.navigationsample.ui.navigate

sealed class NavRoutes(val routes: String) {
    object Home : NavRoutes("home")
    object Welcome : NavRoutes("welcome")
    object Profile : NavRoutes("profile")
}
