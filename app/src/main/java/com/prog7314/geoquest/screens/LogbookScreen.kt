package com.prog7314.geoquest.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.prog7314.geoquest.data.data.LocationData
import com.prog7314.geoquest.data.model.LocationViewModel
import com.prog7314.geoquest.data.model.UserViewModel
import com.prog7314.geoquest.ui.theme.PROG7314Theme
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*

enum class DateFilter {
    ALL, TODAY, THIS_WEEK, THIS_MONTH
}

@Composable
fun LogbookScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    locationViewModel: LocationViewModel = viewModel()
) {
    val currentUser by userViewModel.currentUser.collectAsState()
    val locations by locationViewModel.locations.collectAsState()
    val isLoading by locationViewModel.isLoading.collectAsState()
    var selectedFilter by remember { mutableStateOf(DateFilter.ALL) }

    LaunchedEffect(currentUser?.id, selectedFilter) {
        currentUser?.id?.let { userId ->
            if (userId.isNotBlank()) {
                val calendar = Calendar.getInstance()
                val now = calendar.timeInMillis
                when (selectedFilter) {
                    DateFilter.ALL -> locationViewModel.loadUserLocations(userId)
                    DateFilter.TODAY -> {
                        val startOfDay = getStartOfDay(now)
                        locationViewModel.loadUserLocationsByDateRange(userId, startOfDay, now)
                    }
                    DateFilter.THIS_WEEK -> {
                        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
                        val startOfWeek = getStartOfDay(calendar.timeInMillis)
                        locationViewModel.loadUserLocationsByDateRange(userId, startOfWeek, now)
                    }
                    DateFilter.THIS_MONTH -> {
                        calendar.set(Calendar.DAY_OF_MONTH, 1)
                        val startOfMonth = getStartOfDay(calendar.timeInMillis)
                        locationViewModel.loadUserLocationsByDateRange(userId, startOfMonth, now)
                    }
                }
            }
        }
    }

    LogbookContent(
        navController = navController,
        locations = locations,
        isLoading = isLoading,
        onAddClick = { navController.navigate("add") },
        selectedFilter = selectedFilter,
        onFilterSelected = { selectedFilter = it }
    )
}

@Composable
fun LogbookContent(
    navController: NavController,
    locations: List<LocationData>,
    isLoading: Boolean,
    onAddClick: () -> Unit,
    selectedFilter: DateFilter,
    onFilterSelected: (DateFilter) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Location"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE8F4F8))
                .padding(paddingValues)
        ) {
            Text(
                text = "My Logbook",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            FilterChips(selectedFilter = selectedFilter, onFilterSelected = onFilterSelected)

            Spacer(modifier = Modifier.height(8.dp))

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (locations.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No locations found for this filter.\nTap the '+' button to add one!",
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(locations) { location ->
                        LogbookEntryCard(location = location) {
                            val encodedName = URLEncoder.encode(location.name, StandardCharsets.UTF_8.toString())
                            val encodedDesc = URLEncoder.encode(location.description, StandardCharsets.UTF_8.toString())
                            navController.navigate("home?lat=${location.latitude}&lng=${location.longitude}&name=$encodedName&desc=$encodedDesc")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterChips(selectedFilter: DateFilter, onFilterSelected: (DateFilter) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DateFilter.entries.forEach { filter ->
            val isSelected = selectedFilter == filter
            FilterChip(
                selected = isSelected,
                onClick = { onFilterSelected(filter) },
                label = { Text(filter.name.replace('_', ' ').lowercase().replaceFirstChar { it.uppercase() }) },
                leadingIcon = if (isSelected) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "Selected",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                } else {
                    null
                }
            )
        }
    }
}

// Helper function to get the start of the day for a given timestamp
private fun getStartOfDay(timestamp: Long): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timestamp
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis
}


@Composable
fun LogbookEntryCard(location: LocationData, onClick: () -> Unit) {
    val formattedDate = remember(location.dateAdded) {
        val date = Date(location.dateAdded)
        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = location.name,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = location.description.take(100) + if (location.description.length > 100) "..." else "",
                fontSize = 14.sp,
                color = Color.Gray,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Added on: $formattedDate",
                fontSize = 12.sp,
                color = Color.DarkGray,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LogbookScreenPreview() {
    val mockNavController = rememberNavController()
    val mockLocations = listOf(
        LocationData(id = "1", name = "Eiffel Tower", description = "A visit to the famous landmark in Paris.", dateAdded = System.currentTimeMillis()),
        LocationData(id = "2", name = "Colosseum", description = "Exploring the ancient ruins in Rome.", dateAdded = System.currentTimeMillis() - 86400000)
    )

    PROG7314Theme {
        LogbookContent(
            navController = mockNavController,
            locations = mockLocations,
            isLoading = false,
            onAddClick = {},
            selectedFilter = DateFilter.ALL,
            onFilterSelected = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LogbookScreenEmptyPreview() {
    val mockNavController = rememberNavController()
    PROG7314Theme {
        LogbookContent(
            navController = mockNavController,
            locations = emptyList(),
            isLoading = false,
            onAddClick = {},
            selectedFilter = DateFilter.ALL,
            onFilterSelected = {}
        )
    }
}
