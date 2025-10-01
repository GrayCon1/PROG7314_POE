package com.PROG7314.GeoQuest

import android.R.attr.padding
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.PROG7314.GeoQuest.screens.LoginScreen
import com.PROG7314.GeoQuest.screens.RegisterScreen
import com.PROG7314.GeoQuest.ui.theme.PROG7314Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PROG7314Theme {
                AppNavigation()
            }
        }
    }
}

//@Composable
//fun Main( navController:NavController){
//    NavHost(
//        navController = navController,
//        startDestination = Screen.Register.route,
//        modifier = padding(innerPadding)
//    ) {
//        composable(Screen.Logbook.route) { LogbookScreen() }
//        composable(Screen.Home.route) { HomeScreen() }
//        composable(Screen.Settings.route) { SettingsScreen() }
//        composable(Screen.Login.route) {
//            LoginScreen(
//                onLoginClick = {},
//                onGoogleSignInClick = {},
//                onRegisterClick = {},
//                onForgotPasswordClick = {}
//            )
//        }
//        composable(Screen.Register.route) { RegisterScreen(navController) }
//    }
//}
