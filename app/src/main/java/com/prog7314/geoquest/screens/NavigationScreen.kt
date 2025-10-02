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

@Composable
fun NavigationScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Determine middle button based on current route
    val isOnHome = currentRoute == Screen.Home.route
    val middleScreen = if (isOnHome) Screen.Add else Screen.Home

    val navigationItems = listOf(
        Screen.Logbook,
        middleScreen,
        Screen.Settings
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                navigationItems.forEachIndexed { index, item ->
                    val isMiddleItem = index == 1

                    // For middle item, consider it selected if we're on Home or Add screen
                    val isSelected = if (isMiddleItem) {
                        currentRoute == Screen.Home.route || currentRoute == Screen.Add.route
                    } else {
                        currentRoute == item.route
                    }

                    // Regular spin animation for non-middle items
                    val iconRotation by animateFloatAsState(
                        targetValue = if (isSelected && !isMiddleItem) 360f else 0f,
                        animationSpec = tween(800),
                        label = "iconRotation_${item.route}"
                    )

                    // Flip animation for middle item when switching between Home/Add
                    val flipRotation by animateFloatAsState(
                        targetValue = if (isMiddleItem && isOnHome) 180f else 0f,
                        animationSpec = tween(400),
                        label = "flipRotation"
                    )

                    val iconScale by animateFloatAsState(
                        targetValue = if (isSelected) 1.1f else 1f,
                        animationSpec = tween(500),
                        label = "iconScale_${item.route}"
                    )

                    NavigationBarItem(
                        icon = {
                            Icon(
                                item.icon,
                                contentDescription = item.label,
                                modifier = Modifier
                                    .size(24.dp)
                                    .rotate(iconRotation)
                                    .scale(iconScale)
                                    .graphicsLayer {
                                        if (isMiddleItem) {
                                            rotationY = flipRotation
                                        }
                                    }
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
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen(navController) }
            composable(Screen.Logbook.route) { LogbookScreen(navController) }
            composable(Screen.Add.route) { AddScreen(navController) }
            composable(Screen.Settings.route) { SettingsScreen(navController) }
        }
    }
}
