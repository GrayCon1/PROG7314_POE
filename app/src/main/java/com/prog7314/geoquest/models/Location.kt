package com.prog7314.geoquest.models

data class Location(
    val name: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String,
    val visibility: String
)