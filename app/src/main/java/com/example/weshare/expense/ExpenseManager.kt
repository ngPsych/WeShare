package com.example.weshare.expense

import com.google.firebase.firestore.FirebaseFirestore

class ExpenseManager {
    private val db = FirebaseFirestore.getInstance()

    fun addExpense(expense: Expense, onComplete: (Boolean) -> Unit) {
        db.collection("expenses").add(expense)
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }

    fun getExpensesForGroup(groupId: String, onComplete: (List<Expense>) -> Unit) {
        db.collection("expenses").whereEqualTo("groupId", groupId).get()
            .addOnSuccessListener { result ->
                val expenses = result.mapNotNull { it.toObject(Expense::class.java) }
                onComplete(expenses)
            }
            .addOnFailureListener {
                onComplete(emptyList())
            }
    }

    // Add methods to update, delete expenses
}
