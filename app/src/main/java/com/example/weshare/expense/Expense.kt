package com.example.weshare.expense

data class Expense(
    val groupId: String,
    val totalAmount: Double,
    val description: String,
    val date: Long,
    val creator: String,
    val debts: Map<String, Double>
)
