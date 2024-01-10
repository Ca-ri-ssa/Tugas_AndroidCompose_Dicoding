package com.carissa.compose.ui.screen.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Star : Screen("star")
    object Profile : Screen("profile")
    object Detail : Screen("detail/{detailId}") {
        fun createRoute(detailId: Int) = "detail/$detailId"
    }
}