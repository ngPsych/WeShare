package com.example.weshare.group

data class Group(
    val groupId: String? = null,
    val name: String = "",
    val description: String = "",
    val creator: String = "",
    val members: List<String> = listOf() // Initialize with an empty list
    // Add other relevant group details here
)
