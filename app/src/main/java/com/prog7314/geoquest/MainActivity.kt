package com.prog7314.geoquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.prog7314.geoquest.screens.LoginScreen
import com.prog7314.geoquest.screens.NavigationScreen
import com.prog7314.geoquest.screens.RegisterScreen
import com.prog7314.geoquest.screens.OfflineMapScreen
import com.prog7314.geoquest.screens.IssueListScreen
import com.prog7314.geoquest.screens.Screen
import com.prog7314.geoquest.ui.theme.PROG7314Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PROG7314Theme {
                Main()
            }
        }
    }
}

@Composable
fun Main() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Register.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(Screen.Register.route) {
            RegisterScreen(navController)
        }
        composable(Screen.NavigationScreen.route) {
            NavigationScreen()
        }
        composable(Screen.OfflineMap.route) {
            OfflineMapScreen()
        }
        composable(Screen.Issues.route) {
            IssueListScreen()
        }
    }
}