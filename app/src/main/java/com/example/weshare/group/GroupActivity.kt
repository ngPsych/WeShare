package com.example.weshare.group

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.weshare.R
import com.example.weshare.user.AuthManager

class GroupActivity : AppCompatActivity() {

    private val authManager = AuthManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)

        val groupName = intent.getStringExtra("GROUP_NAME") ?: "Group Name"
        val groupDescription = intent.getStringExtra("GROUP_DESCRIPTION") ?: "Description"

        val groupNameTextView: TextView = findViewById(R.id.groupNameTextView)
        val descriptionTextView: TextView = findViewById(R.id.descriptionTextView)

        groupNameTextView.text = groupName
        descriptionTextView.text = groupDescription

        val nameTextView: TextView = findViewById(R.id.nameTextView)
        nameTextView.text = authManager.getCurrentUserDetails()?.name
        Toast.makeText(this, "${authManager.getCurrentUserDetails()}", Toast.LENGTH_SHORT).show()

    }

    // Add methods to handle group interactions, UI updates, etc.
}
