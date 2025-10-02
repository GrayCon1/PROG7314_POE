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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavController

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(rememberNavController())
}
@Composable
fun SettingsScreen(navController: NavController) {
    var username by remember { mutableStateOf("John Doe") }
    var email by remember { mutableStateOf("johndoe@gmail.com") }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var selectedLanguage by remember { mutableStateOf("English") }
    var isLanguageDropdownExpanded by remember { mutableStateOf(false) }

    val languages = listOf("English", "Spanish", "French", "German", "Portuguese")

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
                                    color = Color(0xFF2C3E50)
                                )
                                Text(
                                    text = "Date Created",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2C3E50)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "John Doe",
                                    color = Color.Gray
                                )
                                Text(
                                    text = "27/07/2025",
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Username Field
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    placeholder = { Text("John Doe", color = Color.Gray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
//                    colors = OutlinedTextFieldDefaults.colors(
//                        focusedBorderColor = Color(0xFF4A90E2),
//                        unfocusedBorderColor = Color(0xFFE0E0E0),
//                        cursorColor = Color(0xFF4A90E2),
//                        containerColor = Color(0xFFF5F5F5)
//                    )
                )

                // Email Field
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("johndoe@gmail.com", color = Color.Gray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
//                    colors = OutlinedTextFieldDefaults.colors(
//                        focusedBorderColor = Color(0xFF4A90E2),
//                        unfocusedBorderColor = Color(0xFFE0E0E0),
//                        cursorColor = Color(0xFF4A90E2),
//                        containerColor = Color(0xFFF5F5F5)
//                    )
                )

                // Current Password Field
                OutlinedTextField(
                    value = currentPassword,
                    onValueChange = { currentPassword = it },
                    placeholder = { Text("Password", color = Color.Gray) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
//                    colors = OutlinedTextFieldDefaults.colors(
//                        focusedBorderColor = Color(0xFF4A90E2),
//                        unfocusedBorderColor = Color(0xFFE0E0E0),
//                        cursorColor = Color(0xFF4A90E2),
//                        containerColor = Color(0xFFF5F5F5)
//                    )
                )

                // New Password Field
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    placeholder = { Text("New Password", color = Color.Gray) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
//                    colors = OutlinedTextFieldDefaults.colors(
//                        focusedBorderColor = Color(0xFF4A90E2),
//                        unfocusedBorderColor = Color(0xFFE0E0E0),
//                        cursorColor = Color(0xFF4A90E2),
//                        containerColor = Color(0xFFF5F5F5)
//                    )
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
                        placeholder = { Text("English", color = Color.Gray) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isLanguageDropdownExpanded = true },
                        shape = RoundedCornerShape(12.dp),
//                        colors = OutlinedTextFieldDefaults.colors(
//                            focusedBorderColor = Color(0xFF4A90E2),
//                            unfocusedBorderColor = Color(0xFFE0E0E0),
//                            cursorColor = Color(0xFF4A90E2),
//                            containerColor = Color(0xFFF5F5F5)
//                        ),
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
                        // Handle save logic here
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(28.dp),
                    border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF4A90E2))
                ) {
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