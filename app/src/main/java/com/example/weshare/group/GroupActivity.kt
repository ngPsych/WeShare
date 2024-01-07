package com.example.weshare.group

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.weshare.R
import com.example.weshare.adapters.GroupAdapter
import com.example.weshare.user.AuthManager
import com.example.weshare.user.UserRepository

class GroupActivity : AppCompatActivity() {

    private val authManager = AuthManager()
    private val userRepository = UserRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)

        val groupName = intent.getStringExtra("GROUP_NAME") ?: "Group Name"
        val groupDescription = intent.getStringExtra("GROUP_DESCRIPTION") ?: "Description"

        val groupNameTextView: TextView = findViewById(R.id.groupNameTextView)
        val descriptionTextView: TextView = findViewById(R.id.descriptionTextView)

        groupNameTextView.text = groupName
        descriptionTextView.text = groupDescription

        userRepository.getUserByEmail(authManager.getCurrentUserDetails()?.email.toString()) { user, _ ->
            user?.let {
                val name = it.name

                val nameTextView: TextView = findViewById(R.id.nameTextView)
                nameTextView.text = name
            }
        }

    }

    // Add methods to handle group interactions, UI updates, etc.
}
