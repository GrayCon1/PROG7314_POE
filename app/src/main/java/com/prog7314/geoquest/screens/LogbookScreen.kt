package com.prog7314.geoquest.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ArrowBack





data class LogbookEntry(
    val id: Int,
    val locationName: String,
    val dateAdded: Date,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)
@Preview(showBackground = true)
@Composable
fun LogbookScreenPreview() {
    LogbookScreen(rememberNavController())
}

@Composable
fun LogbookScreen(
    navController: NavController
) {
    val entries = remember {
        // Sample data - replace with actual data from your repository
        listOf(
            LogbookEntry(1, "Location 1", Date()),
            LogbookEntry(2, "Location 2", Date()),
            LogbookEntry(3, "Location 3", Date()),
            LogbookEntry(4, "Location 4", Date())
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Handle add new logbook entry */ },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Logbook"
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFB0BEC5))
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(entries) { entry ->
                LogbookEntryCard(entry = entry)
            }
        }
    }
}

@Composable
fun LogbookEntryCard(entry: LogbookEntry) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Text content
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = entry.locationName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Date Added:",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        .format(entry.dateAdded),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            // Placeholder for map image
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        Color(0xFFF5F5F5),
                        RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Map",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}


