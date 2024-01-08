package com.example.weshare.group

import com.google.firebase.firestore.FirebaseFirestore

class GroupRepository {

    private val db = FirebaseFirestore.getInstance()

    fun getUserGroups(userEmail: String, onComplete: (List<Group>, String?) -> Unit) {
        db.collection("groups")
            .whereArrayContains("members", userEmail)
            .get()
            .addOnSuccessListener { result ->
                val groups = result.mapNotNull { documentSnapshot ->
                    documentSnapshot.toObject(Group::class.java)
                }
                onComplete(groups, null)
            }
            .addOnFailureListener { exception ->
                onComplete(emptyList(), exception.message)
            }
    }

    fun createGroup(group: Group, onComplete: (Boolean, String?) -> Unit) {
        db.collection("groups").add(group)
            .addOnSuccessListener {
                onComplete(true, null)
            }
            .addOnFailureListener { exception ->
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
                        onComplete(true, null)
                    }.addOnFailureListener { exception ->
                        onComplete(false, exception.message)
                    }
                } else {
                    onComplete(false, "User not found")
                }
            }
            .addOnFailureListener { exception ->
                onComplete(false, exception.message)
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
                    val document = documents.first()
                    val group = document.toObject(Group::class.java)
                    val groupId = document.id
                    onComplete(group, groupId)
                } else {
                    onComplete(null, null)
                }
            }
            .addOnFailureListener {
                onComplete(null, null)
            }
    }

    fun getGroupMembers(groupId: String, onComplete: (List<String>?, String?) -> Unit) {
        val groupRef = db.collection("groups").document(groupId)

        groupRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val group = document.toObject(Group::class.java)
                    val members = group?.members ?: listOf()
                    onComplete(members, null)
                } else {
                    onComplete(null, "Group not found")
                }
            }
            .addOnFailureListener { exception ->
                onComplete(null, exception.message)
            }
    }

    fun removeMemberFromGroup(groupId: String, memberEmail: String, onComplete: (Boolean, String?) -> Unit) {
        val groupRef = db.collection("groups").document(groupId)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(groupRef)
            val group = snapshot.toObject(Group::class.java)
            val currentMembers = group?.members?.toMutableList() ?: mutableListOf()

            if (memberEmail in currentMembers) {
                currentMembers.remove(memberEmail)
                transaction.update(groupRef, "members", currentMembers)
                onComplete(true, null)
            } else {
                onComplete(false, "Member not found in group")
            }
        }.addOnFailureListener { exception ->
            onComplete(false, exception.message)
        }
    }

}
