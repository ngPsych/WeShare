package com.example.weshare.user;

import com.google.firebase.firestore.FirebaseFirestore;

class UserRepository {

    private val db = FirebaseFirestore.getInstance();

    fun getUser(email: String, onComplete: (User?) -> Unit) {
        db.collection("users").document(email).get()
            .addOnSuccessListener { document ->
                val user = document.toObject(User::class.java);
                onComplete(user);
            }
            .addOnFailureListener {
                onComplete(null);
            }
    }

    fun getUserByEmail(email: String, onComplete: (User?) -> Unit) {
        db.collection("users")
            .whereEqualTo("email", email)
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val user = documents.first().toObject(User::class.java)
                    onComplete(user)
                } else {
                    onComplete(null) // No user found
                }
            }
            .addOnFailureListener {
                onComplete(null)
            }
    }

    fun createUser(user: User, onComplete: (Boolean, String?) -> Unit) {
        val newUser = user.copy(userId = null) // Remove userId before saving
        db.collection("users").add(newUser)
            .addOnSuccessListener { documentReference ->
                val userId = documentReference.id
                // Optionally, update the user's userId field in Firestore here
                onComplete(true, userId)
            }
            .addOnFailureListener {
                onComplete(false, null)
            }
    }

    fun updateUser(userId: String, updatedUser: User, onComplete: (Boolean) -> Unit) {
        db.collection("users").document(userId).set(updatedUser.copy(userId = userId))
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }

    fun deleteUser(userId: String, onComplete: (Boolean) -> Unit) {
        db.collection("users").document(userId).delete()
            .addOnSuccessListener {
                onComplete(true);
            }
            .addOnFailureListener {
                onComplete(false);
            }
    }

}
