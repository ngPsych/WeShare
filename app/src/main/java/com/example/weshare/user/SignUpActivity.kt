package com.example.weshare.user;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.weshare.R;
import com.example.weshare.main.MainActivity

class SignUpActivity : AppCompatActivity() {

    private val authManager = AuthManager()
    private val userRepository = UserRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val btnSignUp = findViewById<Button>(R.id.btnSignUp)
        val editTextName: EditText = findViewById(R.id.editTextName)
        val editTextPassword: EditText = findViewById(R.id.editTextPassword)
        val editTextPhoneNumber: EditText = findViewById(R.id.editTextPhoneNumber)
        val editTextEmail: EditText = findViewById(R.id.editTextEmail)

        btnSignUp.setOnClickListener {
            val name = editTextName.text.toString()
            val password = editTextPassword.text.toString()
            val phoneNumber = editTextPhoneNumber.text.toString()
            val email = editTextEmail.text.toString()

            // Input validation (simplified for this example)
            if (name.isEmpty() || password.isEmpty() || phoneNumber.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Proceed with Firebase registration
            authManager.registerUser(email, password) { isSuccessful, errorMessage ->
                if (isSuccessful) {
                    val newUser = User(
                        name = name,
                        phoneNumber = phoneNumber,
                        email = email
                    )

                    userRepository.createUser(newUser) { profileCreationSuccess, userId ->
                        if (profileCreationSuccess) {
                            Toast.makeText(this, "Account created!", Toast.LENGTH_LONG).show()
                            // Use the userId as needed here
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, "Failed to create user profile", Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Registration failed: $errorMessage", Toast.LENGTH_LONG).show()
                }
            }


        }
    }
}
