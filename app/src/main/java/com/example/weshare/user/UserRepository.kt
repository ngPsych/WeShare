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

    fun getUserByEmail(email: String, onComplete: (User?, String?) -> Unit) {
        db.collection("users")
            .whereEqualTo("email", email)
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val document = documents.first() // Get the first (and only) document
                    val user = document.toObject(User::class.java)
                    val userId = document.id // This is the document ID
                    onComplete(user, userId) // Pass the user object and userId (document ID)
                } else {
                    onComplete(null, null) // No user found
                }
            }
            .addOnFailureListener {
                onComplete(null, null)
            }
    }


    fun createUser(user: User, onComplete: (Boolean, String?) -> Unit) {
        db.collection("users").add(user)
            .addOnSuccessListener { documentReference ->
                val generatedUserId = documentReference.id // Firestore-generated ID
                // The generatedUserId can be used if needed, otherwise, it's just confirmation of successful creation
                onComplete(true, generatedUserId)
            }
            .addOnFailureListener {
                onComplete(false, null)
            }
    }

    fun updateUser(userId: String, updatedUser: User, onComplete: (Boolean) -> Unit) {
        db.collection("users").document(userId).set(updatedUser)
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
