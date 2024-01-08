package com.example.weshare.expense

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

class ExpenseRepository {
    private val db = FirebaseFirestore.getInstance()

    fun createExpense(groupId: String, description: String, totalAmount: Double, debts: Map<String, Double>) {
        val expense = hashMapOf(
            "groupId" to groupId,
            "description" to description,
            "totalAmount" to totalAmount,
            "date" to Timestamp(Date()),
            "debts" to debts
        )

        val db = FirebaseFirestore.getInstance()
        db.collection("expenses").add(expense)
            .addOnSuccessListener { documentReference ->
                Log.d("Expense", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("Expense", "Error adding document", e)
            }
    }

    fun calculateDebts(memberIds: List<String>, totalAmount: Double): Map<String, Double> {
        val debts = mutableMapOf<String, Double>()
        val individualAmount = totalAmount / memberIds.size
        memberIds.forEach { memberId ->
            debts[memberId] = individualAmount // Adjust this logic as needed
        }
        return debts
    }

    fun loadExpenses(groupId: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("expenses").whereEqualTo("groupId", groupId)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d("Expense", "${document.id} => ${document.data}")
                        // Process and display the data in your app
                    }
                } else {
                    Log.w("Expense", "Error getting documents.", task.exception)
                }
            }
    }
}