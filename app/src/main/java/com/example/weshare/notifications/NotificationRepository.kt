package com.example.weshare.notifications

import android.util.Log
import com.android.volley.toolbox.StringRequest
import com.example.weshare.group.GroupRepository
import com.example.weshare.user.UserRepository
import com.google.firebase.Firebase
import com.google.firebase.functions.functions
import com.google.firebase.messaging.FirebaseMessaging
import org.json.JSONObject

class NotificationRepository {

    private val userRepository = UserRepository()
    private val functions = Firebase.functions
    private val groupRepository = GroupRepository()

    fun saveFCMToken(email: String) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result

                // Get the current user data
                userRepository.getUserByEmail(email) { user, userId ->
                    if (user != null && userId != null) {
                        // Update the user data with the new FCM token
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

    fun notifyDebtListMembers(groupId: String, debtList: List<String>, title: String, message: String) {
        // Step 1: Retrieve group members
        groupRepository.getGroupMembers(groupId) { members, error ->
            if (error != null) {
                // Handle error
                return@getGroupMembers
            }
            val membersToNotify = members?.filter { it in debtList } ?: listOf()

            // Step 2: Fetch user details for each member in the debt list
            membersToNotify.forEach { memberEmail ->
                userRepository.getUserByEmail(memberEmail) { user, _ ->
                    user?.let {
                        // Step 3: Send notification using FCM token
                        it.fcmToken?.let { it1 -> sendFCMessage(it1, title, message) }
                    }
                }
            }
        }
    }

    private fun sendFCMessage(token: String, title: String, message: String) {
        val data = hashMapOf(
            "title" to title,
            "body" to message,
            "tokens" to listOf(token)
        )

        functions.getHttpsCallable("sendNotification").call(data) // Default name from firebase function.
    }

}