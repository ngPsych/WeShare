package com.example.weshare.group

data class Group(
    val groupId: String? = null,
    val name: String,
    val description: String,
    val creator: String,
    val members: List<String> // List of user IDs
    // Add other relevant group details here
)
