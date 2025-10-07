package com.prog7314.geoquest.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.prog7314.geoquest.data.model.UserViewModel
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.compareTo
import kotlin.text.format

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(rememberNavController())
}

@Composable
fun SettingsScreen(navController: NavController) {
    val userViewModel: UserViewModel = viewModel()
    val currentUser by userViewModel.currentUser.collectAsState()
    val isLoading by userViewModel.isLoading.collectAsState()
    val errorMessage by userViewModel.errorMessage.collectAsState()

    var newPassword by remember { mutableStateOf("") }
    var currentPassword by remember { mutableStateOf("") }
    var selectedLanguage by remember { mutableStateOf("English") }
    var isLanguageDropdownExpanded by remember { mutableStateOf(false) }
    var validationError by remember { mutableStateOf("") }

    val languages = listOf("English", "Spanish", "French", "German", "Portuguese")
    val context = LocalContext.current

    // Check if user is logged in, if not redirect to login
    LaunchedEffect(currentUser) {
        if (currentUser == null) {
            Toast.makeText(context, "Please log in to access settings", Toast.LENGTH_LONG).show()
            navController.navigate("login") {
                popUpTo("settings") { inclusive = true }
            }
        }
    }

    // Show messages from ViewModel
    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            if (message == "Profile updated successfully") {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                newPassword = ""
                currentPassword = ""
            } else {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
            userViewModel.clearError()
        }
    }

    // Create local variable for non-null user
    val user = currentUser
    if (user == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text("Loading user data...")
            }
        }
        return
    }

    // Format date for display
    val formattedDate = user.dateJoined.let { timestamp ->
        val date = Date(timestamp)
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8F4F8))
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .align(Alignment.Center),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Profile Picture
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFB0B0B0))
                            .border(2.dp, Color(0xFF2C3E50), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Profile Picture",
                            modifier = Modifier.size(40.dp),
                            tint = Color(0xFF2C3E50)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // User Info Card
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "User",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2C3E50),
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "Date Created",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2C3E50),
                                    fontSize = 12.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = user.name,
                                    color = Color.Gray,
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = formattedDate,
                                    color = Color.Gray,
                                    fontSize = 11.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Email",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2C3E50),
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = user.email,
                                color = Color.Gray,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Validation Error Message
                if (validationError.isNotEmpty()) {
                    Text(
                        text = validationError,
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                // Non-editable Name Display
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Name & Surname",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = user.name,
                            fontSize = 16.sp,
                            color = Color(0xFF2C3E50)
                        )
                    }
                }

                // Non-editable Email Display
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Email",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = user.email,
                            fontSize = 16.sp,
                            color = Color(0xFF2C3E50)
                        )
                    }
                }

                // Current Password Field
                OutlinedTextField(
                    value = currentPassword,
                    onValueChange = {
                        currentPassword = it
                        validationError = ""
                    },
                    label = { Text("Current Password", color = Color.Gray) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp)
                )

                // New Password Field
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = {
                        newPassword = it
                        validationError = ""
                    },
                    label = { Text("New Password", color = Color.Gray) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp)
                )

                // Language Dropdown
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp)
                ) {
                    OutlinedTextField(
                        value = selectedLanguage,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Language", color = Color.Gray) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isLanguageDropdownExpanded = true },
                        shape = RoundedCornerShape(12.dp),
                        trailingIcon = {
                            IconButton(onClick = { isLanguageDropdownExpanded = true }) {
                                Icon(
                                    Icons.Default.MoreHoriz,
                                    contentDescription = "Language Options",
                                    tint = Color.Gray
                                )
                            }
                        }
                    )

                    DropdownMenu(
                        expanded = isLanguageDropdownExpanded,
                        onDismissRequest = { isLanguageDropdownExpanded = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        languages.forEach { language ->
                            DropdownMenuItem(
                                text = { Text(language) },
                                onClick = {
                                    selectedLanguage = language
                                    isLanguageDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // Save Button
                Button(
                    onClick = {
                        when {
                            currentPassword.isBlank() -> {
                                validationError = "Current password is required to save changes"
                            }
                            newPassword.isNotBlank() && newPassword.length < 6 -> {
                                validationError = "New password must be at least 6 characters"
                            }
                            else -> {
                                if (newPassword.isBlank()) {
                                    validationError = "No changes were made"
                                } else {
                                    userViewModel.updateUser(
                                        userId = user.id,
                                        name = user.name,
                                        email = user.email,
                                        newPassword = newPassword,
                                        currentPassword = currentPassword
                                    )
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(28.dp),
                    border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF4A90E2)),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color(0xFF4A90E2),
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            text = "SAVE",
                            color = Color(0xFF4A90E2),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}


