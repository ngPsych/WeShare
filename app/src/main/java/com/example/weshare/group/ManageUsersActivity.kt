package com.example.weshare.group

import UserAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.weshare.R
import com.example.weshare.main.HomeActivity

class ManageUsersActivity : AppCompatActivity()  {
    private lateinit var memberAdapter: ArrayAdapter<String>
    private val groupRepository = GroupRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_users)

        val groupId = intent.getStringExtra("GROUP_ID") ?: "0"
        val groupName = intent.getStringExtra("GROUP_NAME") ?: "Group Name"
        val groupDescription = intent.getStringExtra("GROUP_DESCRIPTION") ?: "Description"

        val addUserButton: Button = findViewById(R.id.addUserButton)
        val backButton: Button = findViewById(R.id.backButton)
        val membersListView: ListView = findViewById(R.id.membersListView)

        memberAdapter = UserAdapter(this, mutableListOf()) { memberEmail ->
            removeMemberFromGroup(groupId, memberEmail)
        }

        membersListView.adapter = memberAdapter

        loadGroupMembers(groupId)

        addUserButton.setOnClickListener {
            showAddUserDialog(groupName, groupDescription)
        }

        backButton.setOnClickListener {
            navigateToGroup()
        }
    }

    private fun navigateToGroup() {
        val intent = Intent(this, GroupActivity::class.java)
        startActivity(intent)
    }

    private fun loadGroupMembers(groupId: String) {
        groupRepository.getGroupMembers(groupId) { members, error ->
            if (error != null) {
                Toast.makeText(this, "Error fetching members: $error", Toast.LENGTH_SHORT).show()
            } else if (members != null) {
                memberAdapter.clear()
                memberAdapter.addAll(members)
                memberAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun addMemberToGroup(groupId: String, newMemberEmail: String) {
        groupRepository.addMemberToGroup(groupId, newMemberEmail) { success, errorMessage ->
            if (success) {
                loadGroupMembers(groupId)
                Toast.makeText(this, "Member added successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error adding member: $errorMessage", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showAddUserDialog(groupName: String, groupDescription: String) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_user, null)
        val editTextEmailAddress: EditText = dialogView.findViewById(R.id.editTextEmailAddress)

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

    private fun removeMemberFromGroup(groupId: String, memberEmail: String) {
        groupRepository.removeMemberFromGroup(groupId, memberEmail) { success, errorMessage ->
            runOnUiThread {
                if (success) {
                    Toast.makeText(this, "Member removed successfully", Toast.LENGTH_SHORT).show()
                    loadGroupMembers(groupId) // Reload the members list
                } else {
                    Toast.makeText(this, "Error removing member: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}