package com.example.weshare.user

data class User(
    val userId: String? = null,
    val name: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val fcmToken: String = "" // Add this field for the FCM token
)
