package com.example.weshare.main

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.weshare.R
import com.example.weshare.user.AuthManager
import com.example.weshare.user.UserRepository
import com.example.weshare.adapters.GroupAdapter
import com.example.weshare.group.CreateGroupActivity
import com.example.weshare.group.Group
import com.example.weshare.group.GroupActivity
import com.example.weshare.group.GroupRepository
import com.example.weshare.user.ProfileActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeActivity : AppCompatActivity() {

    private val authManager = AuthManager()
    private val userRepository = UserRepository()
    private val groupRepository = GroupRepository()

    private lateinit var groupAdapter: GroupAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val userId = authManager.getCurrentUserDetails()?.userId ?: ""
        val email = authManager.getCurrentUserDetails()?.email ?: ""

        Toast.makeText(this, "Email: $email", Toast.LENGTH_LONG).show()

        val profilePicImageView: ImageView = findViewById(R.id.homeProfilePic)
        val signOutButton: Button = findViewById(R.id.signOutButton)
        val addGroupButton: FloatingActionButton = findViewById(R.id.addGroupButton)
        val groupListView: ListView = findViewById(R.id.groupListView)

        if (userId.isNotEmpty()) {
            userRepository.getUser(email) { user ->
                user?.let {
                    val name = it.name
                    Toast.makeText(this, "Hi $name", Toast.LENGTH_LONG).show()
                }
            }

            userRepository.getUserByEmail(email) { user, _ ->
                user?.let {
                    val phoneNumber = it.phoneNumber // Extracting phone number

                    groupAdapter = GroupAdapter(this, mutableListOf())
                    groupListView.adapter = groupAdapter
                    fetchGroupsAndUpdateAdapter(phoneNumber)
                }
            }

            signOutButton.setOnClickListener {
                authManager.signOut()
                navigateToLogin()
            }

            profilePicImageView.setOnClickListener {
                navigateToProfile()
            }

            addGroupButton.setOnClickListener {
                navigateToCreateGroup()
            }

            groupListView.setOnItemClickListener { parent, view, position, id ->
                val group = parent.getItemAtPosition(position) as Group
                navigateToGroupDetail(group)
            }

        } else {
            navigateToLogin()
        }

    }

    private fun navigateToLogin() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToCreateGroup() {
        val intent = Intent(this, CreateGroupActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToGroupDetail(group: Group) {
        val intent = Intent(this, GroupActivity::class.java).apply {
            putExtra("GROUP_NAME", group.name)
            putExtra("GROUP_DESCRIPTION", group.description)
        }
        startActivity(intent)
    }

    private fun fetchGroupsAndUpdateAdapter(currentUserPhoneNumber: String) {

        groupRepository.getUserGroups(currentUserPhoneNumber) { groups, error ->
            if (error != null) {
                // Handle error, maybe show a message to the user
                print(error)
            } else {
                // Update the adapter with the fetched groups
                runOnUiThread {
                    groupAdapter.updateGroups(groups)
                }
            }
        }
    }

}
