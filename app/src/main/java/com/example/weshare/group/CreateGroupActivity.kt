package com.example.weshare.group

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.weshare.R
import com.example.weshare.main.HomeActivity
import com.example.weshare.user.AuthManager

class CreateGroupActivity : AppCompatActivity() {

    private val groupRepository = GroupRepository()
    private val authManager = AuthManager()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)

        val createGroupButton: Button = findViewById(R.id.createGroupButton)
        val cancelButton: Button = findViewById(R.id.cancelButton)
        val editTextGroupName: EditText = findViewById(R.id.editTextGroupName)
        val editTextDescription: EditText = findViewById(R.id.editTextDescription)

        createGroupButton.setOnClickListener {
            val creatorEmail = authManager.getCurrentUserDetails()?.email ?: ""
            val newGroup = Group(
                name = editTextGroupName.text.toString(),
                description = editTextDescription.text.toString(),
                creator = creatorEmail,
                members = listOf(creatorEmail)
            )

            groupRepository.createGroup(newGroup) { success, errorMessage ->
                if (success) {
                    Toast.makeText(this, "Group created successfully", Toast.LENGTH_SHORT).show()
                    navigateToHome()
                } else {
                    Toast.makeText(this, "Error creating group: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }

        }

        cancelButton.setOnClickListener {
            navigateToHome()
        }
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

}