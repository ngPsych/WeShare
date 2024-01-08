package com.example.weshare.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.weshare.R
import com.example.weshare.notifications.NotificationRepository
import com.example.weshare.user.AuthManager
import com.example.weshare.user.SignUpActivity

class MainActivity : AppCompatActivity() {

    private lateinit var authManager: AuthManager
    private lateinit var notificationRepository: NotificationRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            RequestNotificationPermissionDialog()
        }

        authManager = AuthManager()
        notificationRepository = NotificationRepository()

        val emailEditText: EditText = findViewById(R.id.Email)
        val passwordEditText: EditText = findViewById(R.id.password)
        val loginButton: Button = findViewById(R.id.loginButton)
        val signupButton: Button = findViewById(R.id.signupButton)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email and password cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            authManager.loginUser(email, password) { isSuccessful, errorMessage ->
                if (isSuccessful) {
                    //Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    // Proceed to next activity or operation after login

                    notificationRepository.saveFCMToken(authManager.getCurrentUserDetails()?.email.toString())
                    navigateToHome()
                } else {
                    Toast.makeText(this, "Login failed: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }
        }

        signupButton.setOnClickListener {
            // Navigate to SignUpActivity
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        // Initialize other components like userAdapter if needed
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

}
