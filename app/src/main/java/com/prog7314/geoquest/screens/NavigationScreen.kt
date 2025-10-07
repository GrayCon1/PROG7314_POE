package com.prog7314.geoquest.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavController

@Composable
fun BottomNavigationBar(navController: NavController, currentRoute: String?) {
    val isOnHome = currentRoute == "home"
    val middleScreen = if (isOnHome) Screen.Add else Screen.Home

    val navigationItems = listOf(
        Screen.Logbook,
        middleScreen,
        Screen.Settings
    )

    NavigationBar {
        navigationItems.forEachIndexed { index, item ->
            val isMiddleItem = index == 1
            val isSelected = if (isMiddleItem) {
                currentRoute == "home" || currentRoute == "add"
            } else {
                currentRoute == item.route
            }

            NavigationBarItem(
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label) },
                selected = isSelected,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
}
