package com.prog7314.geoquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.prog7314.geoquest.data.model.UserViewModel
import com.prog7314.geoquest.screens.*
import com.prog7314.geoquest.ui.theme.PROG7314Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirebaseApp.initializeApp(this)
            PROG7314Theme {
                Main()
            }
        }
    }
}

@Composable
fun Main() {
    val navController = rememberNavController()
    val userViewModel: UserViewModel = viewModel()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val routesWithNavBar = listOf("home", "logbook", "add", "settings")
    val showNavBar = currentRoute in routesWithNavBar

    Scaffold(
        bottomBar = {
            if (showNavBar) {
                BottomNavigationBar(navController, currentRoute)
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE8F4F8))
                .padding(innerPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = "login" // Start with login
            ) {
                // Auth screens
                composable(Screen.Login.route) {
                    LoginScreen(navController, userViewModel)
                }
                composable(Screen.Register.route) {
                    RegisterScreen(navController, userViewModel)
                }

                // Main app screens with navigation bar
                composable(Screen.Home.route) {
                    HomeScreen(navController, userViewModel)
                }
                composable(Screen.Logbook.route) {
                    LogbookScreen(navController, userViewModel)
                }
                composable(Screen.Add.route) {
                    AddScreen(navController, userViewModel)
                }
                composable(Screen.Settings.route) {
                    SettingsScreen(navController, userViewModel)
                }
            }
        }
    }
}


