package com.example.weshare.expense

data class Expense(
    val expenseId: String,
    val groupId: String,
    val payerId: String,
    val amount: Double,
    val description: String,
    val timestamp: Long, // Consider using a Date type or a specific format
    val participants: List<String> // List of user IDs of participants involved in the expense
)
