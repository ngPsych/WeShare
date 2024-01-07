package com.example.weshare.group

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
        val addUserButton: Button = findViewById(R.id.addUserButton)

        groupNameTextView.text = groupName
        descriptionTextView.text = groupDescription

        userRepository.getUserByEmail(authManager.getCurrentUserDetails()?.email.toString()) { user, _ ->
            user?.let {
                val name = it.name

                val nameTextView: TextView = findViewById(R.id.nameTextView)
                nameTextView.text = name
            }
        }

        addUserButton.setOnClickListener {
            showAddUserDialog(groupName, groupDescription)
        }

    }

    private fun addMemberToGroup(groupId: String, newMemberEmail: String) {
        groupRepository.addMemberToGroup(groupId, newMemberEmail) { success, errorMessage ->
            if (success) {
                Toast.makeText(this, "Member added successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error adding member: $errorMessage", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showAddUserDialog(groupName: String, groupDescription: String) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_user, null)
        val editTextEmailAddress: EditText = dialogView.findViewById(R.id.editTextEmailAddress)
        val addButton: Button = findViewById(R.id.addButton)

        // Create the AlertDialog
        val dialog = AlertDialog.Builder(this)
            .setTitle("Add User")
            .setView(dialogView)
            .setPositiveButton("Add") { dialog, which ->
                val email = editTextEmailAddress.text.toString()

                groupRepository.getCurrentGroupDetails(groupName, groupDescription) { group, groupId ->
                    if (group != null) {
                        addMemberToGroup(groupId.toString(), email)
                    } else {
                        // Handle the case where no group is found or there's an error
                        Toast.makeText(this, "Group not found or error occurred", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

}
