package com.example.weshare.group

import com.google.firebase.firestore.FirebaseFirestore

class GroupManager {
    private val db = FirebaseFirestore.getInstance()

    fun createGroup(group: Group, onComplete: (Boolean) -> Unit) {
        db.collection("groups").add(group)
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }

    fun getGroup(groupId: String, onComplete: (Group?) -> Unit) {
        db.collection("groups").document(groupId).get()
            .addOnSuccessListener { document ->
                val group = document.toObject(Group::class.java)
                onComplete(group)
            }
            .addOnFailureListener {
                onComplete(null)
            }
    }

    // Add methods to update, delete groups, and manage members
}
