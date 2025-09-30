package com.PROG7314.GeoQuest


import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.MoreHoriz

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Filled.Home)
    object Logbook : Screen("logbook", "Logbook", Icons.Filled.AddCircle)
    object Settings : Screen("settings", "Settings", Icons.Filled.AccountBalanceWallet)
    object Login : Screen("login", "Login", Icons.Filled.MoreHoriz)
    object Register : Screen("register", "Register", Icons.Filled.MoreHoriz)
}

//own files
@Composable
fun HomeScreen() {
    Text("Home")
}

@Composable
fun LogbookScreen() {
    Text("Logbook")
}

@Composable
fun SettingsScreen() {
    Text("Settings")
}


