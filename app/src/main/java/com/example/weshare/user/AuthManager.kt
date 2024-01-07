package com.example.weshare.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthManager {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Register a new user with email and password
    fun registerUser(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null)
                } else {
                    onComplete(false, task.exception?.message)
                }
            }
    }

    // Log in a user with email and password
    fun loginUser(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null)
                } else {
                    onComplete(false, task.exception?.message)
                }
            }
    }

    // Get the current authenticated user
    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    // Retrieve current user's details
    fun getCurrentUserDetails(): User? {
        val firebaseUser = auth.currentUser
        return firebaseUser?.let { user ->
            User(
                userId = user.uid,
                name = user.displayName ?: "Unknown",
                phoneNumber = user.phoneNumber ?: "Unknown",
                email = user.email ?: "Unknown"
            )
        }
    }

    // Update the password for the current user
    fun updatePassword(newPassword: String, onComplete: (Boolean, String?) -> Unit) {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            currentUser.updatePassword(newPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Password update is successful
                        onComplete(true, null)
                    } else {
                        // Password update failed
                        onComplete(false, task.exception?.message)
                    }
                }
        } else {
            // User is not logged in or the user object is null
            onComplete(false, "User not logged in")
        }
    }

    // Delete the current user
    fun deleteUser(onComplete: (Boolean, String?) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            currentUser.delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onComplete(true, null)
                    } else {
                        onComplete(false, task.exception?.message)
                    }
                }
        } else {
            onComplete(false, "User not logged in")
        }
    }

    // Sign out the current user
    fun signOut() {
        auth.signOut()
    }
}
