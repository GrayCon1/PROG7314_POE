package com.prog7314.geoquest.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.prog7314.geoquest.data.model.UserViewModel
import com.prog7314.geoquest.ui.theme.PROG7314Theme
import kotlin.text.get

@Preview
@Composable
fun HomeScreenPreview() {
//    PROG7314Theme { HomeScreen(rememberNavController()) }
}

@Composable
fun HomeScreen(navController: NavController, userViewModel: UserViewModel) {
    MapScreen(userViewModel)
}

@Composable
fun MapScreen(userViewModel: UserViewModel) {
    val context = LocalContext.current
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    var hasLocationPermission by remember { mutableStateOf(false) }
    var showNotifications by remember { mutableStateOf(false) }
    var showFilter by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasLocationPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }

    LaunchedEffect(Unit) {
        hasLocationPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasLocationPermission) {
            permissionLauncher.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ))
        }
    }

    DisposableEffect(hasLocationPermission) {
        var locationCallback: LocationCallback? = null
        if (hasLocationPermission) {
            locationCallback = startLocationUpdates(context) { location ->
                currentLocation = location
            }
        }
        onDispose {
            locationCallback?.let {
                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
                fusedLocationClient.removeLocationUpdates(it)
            }
        }
    }

    val defaultLocation = LatLng(-33.974273681640625, 18.46971893310547)
    val mapCenter = currentLocation ?: defaultLocation

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(mapCenter, 15f)
    }

    LaunchedEffect(currentLocation) {
        currentLocation?.let {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(it, 15f)
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            currentLocation?.let { location ->
                Circle(
                    center = location,
                    radius = 50.0,
                    fillColor = Color.Blue.copy(alpha = 0.3f),
                    strokeColor = Color.Blue,
                    strokeWidth = 2f
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = { showFilter = true },
                modifier = Modifier
                    .background(
                        Color.White.copy(alpha = 0.9f),
                        CircleShape
                    )
                    .size(48.dp)
            ) {
                Icon(
                    Icons.Default.FilterList,
                    contentDescription = "Filter",
                    tint = Color.Black
                )
            }

            IconButton(
                onClick = { showNotifications = true },
                modifier = Modifier
                    .background(
                        Color.White.copy(alpha = 0.9f),
                        CircleShape
                    )
                    .size(48.dp)
            ) {
                Icon(
                    Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = Color.Black
                )
            }
        }

        if (showFilter) {
            FilterOverlay(onDismiss = { showFilter = false })
        }

        if (showNotifications) {
            NotificationOverlay(onDismiss = { showNotifications = false })
        }
    }
}


@SuppressLint("MissingPermission")
private fun startLocationUpdates(
    context: Context,
    onLocationReceived: (LatLng) -> Unit
): LocationCallback {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val locationRequest = LocationRequest.create().apply {
        interval = 5000 // 5 seconds
        fastestInterval = 2000 // 2 seconds
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            result.lastLocation?.let {
                onLocationReceived(LatLng(it.latitude, it.longitude))
            }
        }
    }

    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    return locationCallback
}

@Composable
fun FilterOverlay(onDismiss: () -> Unit) {
    var selectedType by remember { mutableStateOf("Public") }
    var fromDate by remember { mutableStateOf("") }
    var toDate by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(onClick = onDismiss),
        contentAlignment = Alignment.TopCenter
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .clickable(onClick = {}), // Prevent dismiss when clicking inside
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Filter",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C3E50)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Date section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("FROM", fontWeight = FontWeight.Bold)
                    Text("TO", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = fromDate,
                        onValueChange = { fromDate = it },
                        placeholder = { Text("DD/MM/YYYY") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(24.dp)
                    )
                    OutlinedTextField(
                        value = toDate,
                        onValueChange = { toDate = it },
                        placeholder = { Text("DD/MM/YYYY") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(24.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    "TYPE",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Type selection
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FilterTypeButton(
                        text = "Public",
                        isSelected = selectedType == "Public",
                        onClick = { selectedType = "Public" },
                        modifier = Modifier.weight(1f)
                    )
                    FilterTypeButton(
                        text = "Private",
                        isSelected = selectedType == "Private",
                        onClick = { selectedType = "Private" },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun FilterTypeButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFF2C3E50) else Color.White,
            contentColor = if (isSelected) Color.White else Color(0xFF2C3E50)
        ),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(2.dp, Color(0xFF2C3E50))
    ) {
        Text(text)
    }
}

@Composable
fun NotificationOverlay(onDismiss: () -> Unit) {
    val notifications = remember {
        listOf(
            Notification(
                1,
                NotificationType.LOCATION,
                "Location:",
                "Location 1 has been added!",
                Icons.Default.LocationOn
            ),
            Notification(
                2,
                NotificationType.POINTS,
                "Points:",
                "You have earned +5 points",
                Icons.Default.Star
            ),
            Notification(
                3,
                NotificationType.LOCATION,
                "Location:",
                "Location 2 has been added!",
                Icons.Default.LocationOn
            ),
            Notification(
                4,
                NotificationType.POINTS,
                "Points:",
                "You have earned +5 points",
                Icons.Default.Star
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(onClick = onDismiss),
        contentAlignment = Alignment.TopCenter
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .clickable(onClick = {}), // Prevent dismiss when clicking inside
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Button(
                    onClick = { /* Handle clear notifications */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(2.dp, Color(0xFF4A90E2))
                ) {
                    Text(
                        text = "Clear Notification",
                        color = Color(0xFF4A90E2),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.heightIn(max = 400.dp)
                ) {
                    items(notifications) { notification ->
                        NotificationCard(notification = notification)
                    }
                }
            }
        }
    }
}
