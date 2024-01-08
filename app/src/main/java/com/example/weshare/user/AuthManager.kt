package com.example.weshare.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class AuthManager {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

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

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

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

    fun updatePassword(newPassword: String, onComplete: (Boolean, String?) -> Unit) {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            currentUser.updatePassword(newPassword)
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

    fun signOut() {
        auth.signOut()
    }
}
