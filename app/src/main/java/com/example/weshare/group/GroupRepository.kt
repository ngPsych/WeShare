package com.example.weshare.group

import com.google.firebase.firestore.FirebaseFirestore

class GroupRepository {

    private val db = FirebaseFirestore.getInstance()

    fun getAllGroups(onComplete: (List<Group>) -> Unit) {
        db.collection("groups").get()
            .addOnSuccessListener { result ->
                val groups = result.mapNotNull { it.toObject(Group::class.java) }
                onComplete(groups)
            }
            .addOnFailureListener {
                onComplete(emptyList())
            }
    }

    fun getUserGroups(creatorEmail: String, onComplete: (List<Group>, String?) -> Unit) {
        db.collection("groups")
            .whereEqualTo("creator", creatorEmail)
            .get()
            .addOnSuccessListener { result ->
                val groups = result.mapNotNull { documentSnapshot ->
                    documentSnapshot.toObject(Group::class.java)
                }
                onComplete(groups, null) // Return the list of groups and no error message
            }
            .addOnFailureListener { exception ->
                onComplete(emptyList(), exception.message) // Return an empty list and the error message
            }
    }


    // Add methods to interact with group data, like getting specific groups, updating groups, etc.
}
