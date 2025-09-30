package com.PROG7314.GeoQuest

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.PROG7314.GeoQuest.screens.LoginScreen
import com.PROG7314.GeoQuest.screens.RegisterScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val items = listOf(
         Screen.Logbook,Screen.Home,  Screen.Settings
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = currentRoute == item.route,
                        onClick = {
                            if (currentRoute != item.route) {
                                navController.navigate(item.route) {
                                    launchSingleTop = true
                                }
                            }
                        })
                }
            }
        }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Register.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Logbook.route) { LogbookScreen() }
            composable(Screen.Home.route) { HomeScreen() }
            composable(Screen.Settings.route) { SettingsScreen() }
            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginClick = {},
                    onGoogleSignInClick = {},
                    onRegisterClick = {},
                    onForgotPasswordClick = {}
                )
            }
            composable(Screen.Register.route) { RegisterScreen(navController) }
        }
    }
}