package com.example.weshare.user

data class User(
    val userId: String = "",
    val name: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val fcmToken: String? = ""
)
