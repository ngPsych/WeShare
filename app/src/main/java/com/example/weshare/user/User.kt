package com.example.weshare.user

data class User(
    val userId: String? = null, // Make it nullable and default to null
    val name: String,
    val phoneNumber: String,
    val email: String
    // Add other relevant user details here
)
