package com.prog7314.geoquest.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
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
import com.prog7314.geoquest.ui.theme.PROG7314Theme

@Preview
@Composable
fun HomeScreenPreview() {
    PROG7314Theme { HomeScreen(rememberNavController()) }
}

@Composable
fun HomeScreen(navController: NavController) {
    MapScreen()
}

@Composable
fun MapScreen() {
    val context = LocalContext.current
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    var hasLocationPermission by remember { mutableStateOf(false) }

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

    // Update camera position when location changes
    LaunchedEffect(currentLocation) {
        currentLocation?.let {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(it, 15f)
            )
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ){
        currentLocation?.let { location ->
            Circle(
                center = location,
                radius = 50.0, // 50 meters radius
                fillColor = Color.Blue.copy(alpha = 0.3f),
                strokeColor = Color.Blue,
                strokeWidth = 2f
            )
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
