package com.example.weshare.utils

import android.content.Context
import android.widget.Toast
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    fun formatCurrency(amount: Double): String {
        val formatter = NumberFormat.getCurrencyInstance()
        return formatter.format(amount)
    }

    fun formatDate(timestamp: Long): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formatter.format(Date(timestamp))
    }

    fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, duration).show()
    }

    object ValidationUtils {

        fun isValidEmail(email: String): Boolean {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        // Add other validation functions here, like password strength, phone number format, etc.
    }

    // Add other utility functions here
}
