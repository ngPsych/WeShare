package com.example.weshare.expense

data class Expense(
    val groupId: String,
    val totalAmount: Double,
    val description: String,
    val date: Long, // Consider using a Date type or a specific format
    val debts: Map<String, Double> // List of user IDs of participants involved in the expense
)
