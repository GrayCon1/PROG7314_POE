package com.prog7314.geoquest.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Filled.Home)
    object Logbook : Screen("logbook", "Logbook", Icons.Filled.AddCircle)
    object Settings : Screen("settings", "Settings", Icons.Filled.AccountBalanceWallet)
    object Login : Screen("login", "Login", Icons.Filled.MoreHoriz)
    object Register : Screen("register", "Register", Icons.Filled.MoreHoriz)
    object Add : Screen("add", "Add", Icons.Filled.AddCircle)
    object NavigationScreen : Screen("navigation_screen", "Navigation", Icons.Filled.MoreHoriz)
    object OfflineMap : Screen("offline_map", "Offline Map", Icons.Filled.MoreHoriz)
}