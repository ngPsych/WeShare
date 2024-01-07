package com.example.weshare.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
//import com.example.weshare.databinding.ItemExpenseBinding
import com.example.weshare.expense.Expense

/*
class ExpenseAdapter(private val expenses: List<Expense>) : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val binding = ItemExpenseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExpenseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenses[position]
        holder.bind(expense)
    }

    override fun getItemCount() = expenses.size

    class ExpenseViewHolder(private val binding: ItemExpenseBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(expense: Expense) {
            binding.textViewExpenseDescription.text = expense.description
            binding.textViewAmount.text = "${expense.amount}"
            // Format and display the date and other properties as needed
        }
    }
}

 */

class ExpenseAdapter() {

}