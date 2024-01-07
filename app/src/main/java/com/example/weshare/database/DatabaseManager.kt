package com.example.weshare.database

import com.google.firebase.firestore.FirebaseFirestore
import com.example.weshare.user.User
import com.example.weshare.group.Group
import com.example.weshare.expense.Expense

class DatabaseManager {
    private val db = FirebaseFirestore.getInstance()

    /*
    // User-related operations
    fun createUser(user: User, onComplete: (Boolean) -> Unit) {
        db.collection("users").document(user.userId).set(user)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun getUser(userId: String, onComplete: (User?) -> Unit) {
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                val user = document.toObject(User::class.java)
                onComplete(user)
            }
            .addOnFailureListener { onComplete(null) }
    }

    // Group-related operations
    fun createGroup(group: Group, onComplete: (Boolean) -> Unit) {
        db.collection("groups").document(group.groupId).set(group)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    // Expense-related operations
    fun addExpense(expense: Expense, onComplete: (Boolean) -> Unit) {
        db.collection("expenses").add(expense)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

     */

    // Add more methods for updating, deleting, and querying data as needed
}
