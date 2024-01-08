package com.example.weshare.group

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.example.weshare.R
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

        addExpenseButton.setOnClickListener {
            groupRepository.getCurrentGroupDetails(groupName, groupDescription) { group, groupId ->
                if (group != null && groupId != null) {
                    showAddExpenseDialog(groupId)
                } else {
                    Toast.makeText(this, "Error: Group not found.", Toast.LENGTH_SHORT).show()
                }
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

    private fun showAddExpenseDialog(groupId: String) {
        groupRepository.getGroupMembers(groupId) { members, error ->
            if (members != null && error == null) {
                val builder = AlertDialog.Builder(this)
                val inflater = layoutInflater
                val dialogView = inflater.inflate(R.layout.dialog_add_expense, null)
                builder.setView(dialogView)

                val expenseDescription = dialogView.findViewById<EditText>(R.id.expenseDescription)
                val expenseAmount = dialogView.findViewById<EditText>(R.id.expenseAmount)
                val membersContainer = dialogView.findViewById<LinearLayout>(R.id.membersContainer)

                // Dynamically add views for each member
                members.forEach { memberName ->
                    val memberDebt = EditText(this)
                    memberDebt.hint = "$memberName's debt"
                    memberDebt.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                    membersContainer.addView(memberDebt)
                }

                builder.setPositiveButton("Save") { dialog, _ ->
                    val description = expenseDescription.text.toString()
                    val amount = expenseAmount.text.toString().toDoubleOrNull() ?: 0.0
                    val debts = mutableMapOf<String, Double>()
                    membersContainer.children.forEachIndexed { index, view ->
                        val debtAmount = (view as EditText).text.toString().toDoubleOrNull() ?: 0.0
                        debts[members[index]] = debtAmount
                    }

                    // Call your method to create or update the expense
                    //createOrUpdateExpense(description, amount, debts)
                    dialog.dismiss()
                }

                builder.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }

                val dialog = builder.create()
                dialog.show()
            } else {
                // Handle error or group not found scenario
                Toast.makeText(this, error ?: "Error fetching group members", Toast.LENGTH_SHORT).show()
            }
        }
    }




}
