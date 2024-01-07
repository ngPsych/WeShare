package com.example.weshare.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.weshare.R
import com.example.weshare.user.AuthManager
import com.example.weshare.user.SignUpActivity

class MainActivity : AppCompatActivity() {

    private lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        authManager = AuthManager()

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
