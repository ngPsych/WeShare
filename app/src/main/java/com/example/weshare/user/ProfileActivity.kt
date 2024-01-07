package com.example.weshare.user

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.weshare.R
import com.example.weshare.main.HomeActivity


class ProfileActivity : AppCompatActivity() {

    private val authManager = AuthManager()
    private val userRepository = UserRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val email = authManager.getCurrentUserDetails()?.email ?: ""

        val profileName: TextView = findViewById(R.id.profileName)
        val profileNotificationSwitch: Switch = findViewById(R.id.profileNotificationSwitch)
        val profileEmail: TextView = findViewById(R.id.profileEmail)
        val profilePhoneNumber: EditText = findViewById(R.id.profilePhoneNumber)
        val profilePassword: EditText = findViewById(R.id.profilePassword)
        val profileConfirmPassword: EditText = findViewById(R.id.profileConfirmPassword)
        val profileUpdateButton: Button = findViewById(R.id.profileUpdateButton)
        val profileCancelButton: Button = findViewById(R.id.profileCancelButton)

        userRepository.getUserByEmail(email) { user, userId ->
            user?.let {
                if (user != null) {
                    profileName.text = user.name
                    profilePhoneNumber.hint = user.phoneNumber
                    profileEmail.hint = user.email
                    Toast.makeText(this,
                        "$userId, ${user.name}, ${user.phoneNumber}, ${user.email}", Toast.LENGTH_SHORT).show()

                    profileUpdateButton.setOnClickListener {
                        var newPhoneNumber = user.phoneNumber // default to the current phone number

                        if (profilePhoneNumber.text.isNotEmpty()) {
                            newPhoneNumber = profilePhoneNumber.text.toString()
                        } else {
                            Toast.makeText(this, "EMAIL IS EMPTY", Toast.LENGTH_SHORT).show()
                        }

                        val newPassword = profilePassword.text.toString()
                        val confirmPassword = profileConfirmPassword.text.toString()

                        if (newPassword == confirmPassword && newPassword.isNotEmpty()) {
                            updatePassword(newPassword)
                        } else {
                            Toast.makeText(this, "PASSWORD IS EMPTY OR DOES NOT MATCH", Toast.LENGTH_SHORT).show()
                        }

                        // Create an updated user object
                        val updatedUser = User(
                            name = user.name,
                            phoneNumber = newPhoneNumber,
                            email = user.email
                        )

                        // Call updateUser to update the user's information in Firestore
                        userRepository.updateUser(userId.toString(), updatedUser) { isSuccess ->
                            if (isSuccess) {
                                Toast.makeText(
                                    this,
                                    "User updated successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(this, "Failed to update user", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }

                }
            }
        }

        profileCancelButton.setOnClickListener {
            navigateToHome()
        }

    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    // Add methods to update user profile, handle inputs, etc
    private fun updatePassword(newPassword: String) {
        if (newPassword.isNotEmpty()) {
            authManager.updatePassword(newPassword) { isSuccess, errorMessage ->
                if (isSuccess) {
                    //Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show()
                    // Handle successful password update (e.g., navigate back to the previous screen)
                } else {
                    //Toast.makeText(this, "Failed to update password: $errorMessage", Toast.LENGTH_LONG).show()
                    // Handle failure (e.g., show error message to the user)
                }
            }
        } else {
            Toast.makeText(this, "Please enter a new password", Toast.LENGTH_SHORT).show()
        }
    }

}
