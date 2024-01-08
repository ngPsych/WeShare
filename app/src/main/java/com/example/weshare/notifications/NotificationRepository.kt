package com.example.weshare.notifications

import android.util.Log
import com.example.weshare.group.GroupRepository
import com.example.weshare.user.UserRepository
import com.google.firebase.Firebase
import com.google.firebase.functions.functions
import com.google.firebase.messaging.FirebaseMessaging

class NotificationRepository {


    private val userRepository = UserRepository()

    fun saveFCMToken(email: String) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result

                userRepository.getUserByEmail(email) { user, userId ->
                    if (user != null && userId != null) {
                        val updatedUser = user.copy(fcmToken = token)
                        userRepository.updateUser(userId, updatedUser) { success ->
                            if (success) {
                                Log.d("FCM Token", "Token successfully updated in Firestore")
                            } else {
                                Log.e("FCM Token", "Failed to update token in Firestore")
                            }
                        }
                    } else {
                        Log.e("FCM Token", "User not found")
                    }
                }
            } else {
                Log.e("FCM Token", "Failed to retrieve FCM token")
            }
        }

    }
}