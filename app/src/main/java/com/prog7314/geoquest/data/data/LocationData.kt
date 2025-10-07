package com.prog7314.geoquest.data.data

data class LocationData(
    val id: String = "",
    val userId: String = "",
    val name: String = "",
    val description: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val imageUri: String? = null,
    val visibility: String = "public",  // or "private"
    val dateAdded: Long = System.currentTimeMillis()
)