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

    fun createGroup(group: Group, onComplete: (Boolean, String?) -> Unit) {
        db.collection("groups").add(group)
            .addOnSuccessListener {
                // Group created successfully
                onComplete(true, null)
            }
            .addOnFailureListener { exception ->
                // Error in creating group
                onComplete(false, exception.message)
            }
    }

    fun addMemberToGroup(groupId: String, newMemberEmail: String, onComplete: (Boolean, String?) -> Unit) {
        val usersRef = db.collection("users")
        val groupRef = db.collection("groups").document(groupId)

        // Check if the user exists
        usersRef.whereEqualTo("email", newMemberEmail).limit(1).get()
            .addOnSuccessListener { users ->
                if (!users.isEmpty) {
                    // User exists, proceed to add to group
                    db.runTransaction { transaction ->
                        val snapshot = transaction.get(groupRef)
                        val group = snapshot.toObject(Group::class.java)
                        val currentMembers = group?.members ?: listOf()

                        if (newMemberEmail !in currentMembers) {
                            val updatedMembers = currentMembers + newMemberEmail
                            transaction.update(groupRef, "members", updatedMembers)
                        }
                    }.addOnSuccessListener {
                        onComplete(true, null) // Member added successfully
                    }.addOnFailureListener { exception ->
                        onComplete(false, exception.message) // Error in adding member
                    }
                } else {
                    onComplete(false, "User not found") // User does not exist
                }
            }
            .addOnFailureListener { exception ->
                onComplete(false, exception.message) // Error checking user existence
            }
    }

    fun getCurrentGroupDetails(groupName: String, groupDescription: String, onComplete: (Group?, String?) -> Unit) {
        db.collection("groups")
            .whereEqualTo("name", groupName)
            .whereEqualTo("description", groupDescription)
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val document = documents.first() // Get the first (and only) document
                    val group = document.toObject(Group::class.java)
                    val groupId = document.id // This is the document ID
                    onComplete(group, groupId) // Pass the Group object and its ID
                } else {
                    onComplete(null, null) // No group found with the given name and description
                }
            }
            .addOnFailureListener {
                onComplete(null, null) // Error occurred during fetching
            }
    }
}
