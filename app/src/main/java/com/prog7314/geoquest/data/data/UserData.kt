package com.prog7314.geoquest.data.data

data class UserData(
    val id: String = "",
    val name: String = "",
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val dateJoined: Long = System.currentTimeMillis()
)