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
        val phoneNumber = authManager.getCurrentUserDetails()?.phoneNumber ?: ""
        //Toast.makeText(this, "UserID: $userId", Toast.LENGTH_LONG).show()
        print("UserID: $userId")

        val profilePicImageView: ImageView = findViewById(R.id.homeProfilePic)
        val signOutButton: Button = findViewById(R.id.signOutButton)
        val addGroupButton: FloatingActionButton = findViewById(R.id.addGroupButton)
        val groupListView: ListView = findViewById(R.id.groupListView)
        val currentUserPhoneNumber = authManager.getCurrentUserDetails()?.phoneNumber ?: ""

        groupAdapter = GroupAdapter(this, listOf())
        groupListView.adapter = groupAdapter

        if (userId.isNotEmpty()) {
            userRepository.getUser(phoneNumber) { user ->
                user?.let {
                    val name = it.name
                    Toast.makeText(this, "Hi $name", Toast.LENGTH_LONG).show()
                }
            }

            fetchUserAndGroups(currentUserPhoneNumber)

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

    private fun fetchUserAndGroups(phoneNumber: String) {
        groupRepository.getUserGroups(phoneNumber) { groups, errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(this, "Error fetching groups: $errorMessage", Toast.LENGTH_LONG).show()
            } else {
                groupAdapter.clear()
                groupAdapter.addAll(groups)
                groupAdapter.notifyDataSetChanged()
            }
        }
    }

}
