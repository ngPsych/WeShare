package com.example.weshare.expense

import android.util.Log
import com.example.weshare.group.Group
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

class ExpenseRepository {
    private val db = FirebaseFirestore.getInstance()

    fun createExpense(groupId: String, description: String, totalAmount: Double, creator: String, debts: Map<String, Double>) {
        val expense = hashMapOf(
            "groupId" to groupId,
            "description" to description,
            "totalAmount" to totalAmount,
            "date" to Timestamp(Date()),
            "creator" to creator,
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

    fun calculateMemberTotalDebtInGroup(memberEmail: String, groupId: String, onComplete: (Boolean, Double) -> Unit) {

        // Step 1: Retrieve expenses for the given groupId
        db.collection("expenses").whereEqualTo("groupId", groupId).get()
            .addOnSuccessListener { expenses ->
                var totalDebt = 0.0

                // Step 2: Check each expense for member's involvement and accumulate debts
                expenses.forEach { expense ->
                    val debts = expense.data["debts"] as Map<String, Double>
                    totalDebt += debts[memberEmail] ?: 0.0
                }

                // Step 3: Verify the member is part of the group and return the total debt
                db.collection("groups").document(groupId).get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            val group = document.toObject(Group::class.java)
                            val isMemberInGroup = memberEmail in (group?.members ?: listOf())
                            onComplete(isMemberInGroup, if (isMemberInGroup) totalDebt else 0.0)
                        } else {
                            onComplete(false, 0.0) // Group not found
                        }
                    }
                    .addOnFailureListener {
                        onComplete(false, 0.0) // Error in fetching group
                    }
            }
            .addOnFailureListener {
                onComplete(false, 0.0) // Error in fetching expenses
            }
    }

    fun calculateTotalOwedToCreator(creatorName: String, groupId: String, onComplete: (Boolean, Double) -> Unit) {

        // Step 1: Retrieve expenses for the given groupId created by the creator
        db.collection("expenses")
            .whereEqualTo("groupId", groupId)
            .whereEqualTo("creator", creatorName)
            .get()
            .addOnSuccessListener { expenses ->
                var totalOwed = 0.0

                // Calculate the sum of all debts owed to the creator
                expenses.forEach { expense ->
                    val debts = expense.data["debts"] as Map<String, Double>
                    debts.filterKeys { it != creatorName }.forEach { (_, amount) ->
                        totalOwed += amount
                    }
                }

                // Step 2: Verify the creator is part of the group
                db.collection("groups").document(groupId).get()
                    .addOnSuccessListener { document ->
                        val isCreatorInGroup = document.exists() && creatorName in (document.toObject(Group::class.java)?.members ?: listOf())
                        onComplete(isCreatorInGroup, totalOwed)
                    }
                    .addOnFailureListener {
                        onComplete(false, 0.0) // Error in fetching group
                    }
            }
            .addOnFailureListener {
                onComplete(false, 0.0) // Error in fetching expenses
            }
    }

}