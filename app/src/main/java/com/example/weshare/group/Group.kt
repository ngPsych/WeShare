package com.example.weshare.group

data class Group(
    val groupId: String? = null,
    val name: String = "",
    val description: String = "",
    val creator: String = "",
    val members: List<String> = listOf()
)
