package com.example.weshare.user

data class User(
    val userId: String? = null, // Default values for all properties
    val name: String = "",
    val phoneNumber: String = "",
    val email: String = ""
    // Add other relevant user details with default values
)
