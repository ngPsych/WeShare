package com.example.weshare.user

data class User(
    val userId: String = "",
    val name: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val fcmToken: String? = null // Add this field for the FCM token
)
