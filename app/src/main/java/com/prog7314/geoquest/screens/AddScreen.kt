package com.prog7314.geoquest.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.prog7314.geoquest.data.data.LocationData
import com.prog7314.geoquest.data.model.LocationViewModel
import com.prog7314.geoquest.data.model.UserViewModel

@Preview
@Composable
fun AddScreenPreview() {
//    AddScreen(rememberNavController())
}

@SuppressLint("MissingPermission")
@Composable
fun AddScreen(navController: NavController, userViewModel: UserViewModel) {
    val locationViewModel: LocationViewModel = viewModel()
    val currentUser by userViewModel.currentUser.collectAsState()

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isPublic by remember { mutableStateOf(true) }
    var selectedImageUri by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val currentDeviceLocation = remember { mutableStateOf<LatLng?>(null) }
    val isLoading by userViewModel.isLoading.collectAsState()
    val errorMessage by userViewModel.errorMessage.collectAsState()

    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasLocationPermission = isGranted
            if (isGranted) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        currentDeviceLocation.value = LatLng(location.latitude, location.longitude)
                    }
                }
            }
        }
    )

    LaunchedEffect(hasLocationPermission) {
        if (hasLocationPermission) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    currentDeviceLocation.value = LatLng(location.latitude, location.longitude)
                }
            }
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }


    // Handle error messages
    errorMessage?.let { error ->
        LaunchedEffect(error) {
            // Show error (you can use a Snackbar or Toast)
            userViewModel.clearError()
        }
    }

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            selectedImageUri = it.toString()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Main content card
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 20.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Image upload section
                ImageUploadSection(
                    selectedImageUri = selectedImageUri,
                    onImageClick = {
                        imagePickerLauncher.launch("image/*")
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Form section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Name field
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            placeholder = { Text("Name") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White,
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = Color(0xFF87CEEB)
                            )
                        )

                        // Description field
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            placeholder = { Text("Description") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White,
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = Color(0xFF87CEEB)
                            ),
                            maxLines = 4
                        )

                        // Public/Private toggle
                        VisibilityToggle(
                            isPublic = isPublic,
                            onToggle = { isPublic = it }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Google Maps placeholder with save button
                GoogleMapsPlaceholder(
                    onSaveLocation = {
                        val userId = currentUser?.id
                        val lat = currentDeviceLocation.value?.latitude
                        val long = currentDeviceLocation.value?.longitude

                        if (userId.isNullOrBlank()) {
                            Toast.makeText(
                                context,
                                "Error: User not logged in.",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@GoogleMapsPlaceholder
                        }

                        if (lat == null || long == null) {
                            Toast.makeText(
                                context,
                                "Error: Could not get current location.",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@GoogleMapsPlaceholder
                        }

                        val locationData = LocationData(
                            userId = userId,
                            name = name,
                            description = description,
                            latitude = lat,
                            longitude = long,
                            imageUri = selectedImageUri,
                            visibility = if (isPublic) "public" else "private"
                        )

                        locationViewModel.addLocation(locationData)

                        // Navigate back or show success message
                        navController.popBackStack()
                        Toast
                            .makeText(context, "Location Added", Toast.LENGTH_SHORT)
                            .show()
                    },
                    canSave = name.isNotBlank() && description.isNotBlank() && selectedImageUri != null
                )

                Spacer(modifier = Modifier.weight(1f))

                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color(0xFF87CEEB),
                        modifier = Modifier.padding(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}


@Composable
fun ImageUploadSection(
    selectedImageUri: String?,
    onImageClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFC0C0C0))
            .clickable { onImageClick() },
        contentAlignment = Alignment.Center
    ) {
        if (selectedImageUri != null) {
            // Here you would display the selected image
            // For now, showing placeholder text
            Text(
                text = "Image Selected",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        } else {
            Text(
                text = "Tap To Add Image",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun VisibilityToggle(
    isPublic: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Public button
        Button(
            onClick = { onToggle(true) },
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isPublic) Color(0xFF87CEEB) else Color.White
            ),
            border = if (!isPublic) BorderStroke(1.dp, Color.Gray) else null
        ) {
            Text(
                text = "Public",
                color = if (isPublic) Color.White else Color.Black,
                fontWeight = FontWeight.Medium
            )
        }

        // Private button
        Button(
            onClick = { onToggle(false) },
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (!isPublic) Color(0xFF87CEEB) else Color.White
            ),
            border = if (isPublic) BorderStroke(1.dp, Color.Gray) else null
        ) {
            Text(
                text = "Private",
                color = if (!isPublic) Color.White else Color.Black,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun GoogleMapsPlaceholder(onSaveLocation: () -> Unit = {}, canSave: Boolean = true) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Map placeholder content
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Google Maps Component\n(To be implemented)",
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Save location button at bottom
            Button(
                onClick = onSaveLocation, // Correctly assign the function to onClick
                enabled = canSave,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .height(40.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF87CEEB)
                )
            ) {
                Text(
                    text = "Save Location",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
