package com.example.weshare.group

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.weshare.R
import com.example.weshare.adapters.GroupAdapter
import com.example.weshare.main.HomeActivity
import com.example.weshare.user.AuthManager
import com.example.weshare.user.UserRepository

class GroupActivity : AppCompatActivity() {

    private val authManager = AuthManager()
    private val userRepository = UserRepository()
    private val groupRepository = GroupRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)

        val groupName = intent.getStringExtra("GROUP_NAME") ?: "Group Name"
        val groupDescription = intent.getStringExtra("GROUP_DESCRIPTION") ?: "Description"

        val groupNameTextView: TextView = findViewById(R.id.groupNameTextView)
        val descriptionTextView: TextView = findViewById(R.id.descriptionTextView)
        val addExpenseButton: Button = findViewById(R.id.addExpenseButton)
        val manageUsersButton: Button = findViewById(R.id.manageUsersButton)
        val backButton: Button = findViewById(R.id.backButton)

        groupNameTextView.text = groupName
        descriptionTextView.text = groupDescription

        userRepository.getUserByEmail(authManager.getCurrentUserDetails()?.email.toString()) { user, _ ->
            user?.let {
                val name = it.name

                val nameTextView: TextView = findViewById(R.id.nameTextView)
                nameTextView.text = name
            }
        }

        manageUsersButton.setOnClickListener {
            navigateToManageUsers(groupName, groupDescription)
        }

        backButton.setOnClickListener {
            navigateToHome()
        }

    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToManageUsers(groupName: String, groupDescription: String) {
        groupRepository.getCurrentGroupDetails(groupName, groupDescription) { group, groupId ->
            if (group != null) {
                val intent = Intent(this, ManageUsersActivity::class.java).apply {
                    putExtra("GROUP_ID", groupId)
                    putExtra("GROUP_NAME", groupName)
                    putExtra("GROUP_DESCRIPTION", groupDescription)
                }
                startActivity(intent)
            } else {
                // Handle the case where no group is found or there's an error
                Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show()
            }
        }


    }

}
