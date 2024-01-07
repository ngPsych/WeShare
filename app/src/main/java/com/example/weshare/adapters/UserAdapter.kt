package com.example.weshare.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weshare.user.User
import com.example.weshare.databinding.ItemUserBinding

class UserAdapter(var users: MutableList<User>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)
    }

    override fun getItemCount() = users.size

    class UserViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.userNameTextView.text = user.name
            // Bind other views in the holder as needed
        }
    }

    fun updateUsers(newUsers: List<User>) {
        this.users.clear() // Clears the current list
        this.users.addAll(newUsers) // Adds all the new users
        this.notifyDataSetChanged() // Notifies the adapter to refresh the list
    }

    interface OnUserClickListener {
        fun onUserClick(user: User)
    }
}
