package com.prog7314.geoquest.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Preview(showBackground = true)
@Composable
fun FilterScreenPreview() {
    FilterScreen(rememberNavController())
}

@Composable
fun FilterScreen(navController: NavController) {
    var fromDate by remember { mutableStateOf("") }
    var toDate by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("Public") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8F4F8))
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .align(Alignment.TopCenter),
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
                // Filter Title
                Text(
                    text = "Filter",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C3E50),
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // Date Range Section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "FROM",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C3E50)
                    )
                    Text(
                        text = "TO",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C3E50)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // From Date Field
                    OutlinedTextField(
                        value = fromDate,
                        onValueChange = { fromDate = it },
                        placeholder = { Text("DD/MM/YYYY", color = Color.Gray) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(25.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4A90E2),
                            unfocusedBorderColor = Color(0xFF4A90E2)
                        ),
                        trailingIcon = {
                            Icon(
                                Icons.Default.DateRange,
                                contentDescription = "Select Date",
                                tint = Color.Gray
                            )
                        }
                    )

                    // To Date Field
                    OutlinedTextField(
                        value = toDate,
                        onValueChange = { toDate = it },
                        placeholder = { Text("DD/MM/YYYY", color = Color.Gray) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(25.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4A90E2),
                            unfocusedBorderColor = Color(0xFF4A90E2)
                        ),
                        trailingIcon = {
                            Icon(
                                Icons.Default.DateRange,
                                contentDescription = "Select Date",
                                tint = Color.Gray
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Type Section
                Text(
                    text = "TYPE",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C3E50),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Type Selection Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TypeButton(
                        text = "Public",
                        isSelected = selectedType == "Public",
                        onClick = { selectedType = "Public" },
                        modifier = Modifier.weight(1f)
                    )
                    TypeButton(
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
fun TypeButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .background(
                color = if (isSelected) Color(0xFF4A90E2) else Color.White,
                shape = RoundedCornerShape(24.dp)
            )
            .border(
                width = 2.dp,
                color = Color(0xFF4A90E2),
                shape = RoundedCornerShape(24.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else Color(0xFF4A90E2),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
