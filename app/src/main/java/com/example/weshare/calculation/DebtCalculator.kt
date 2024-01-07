package com.example.weshare.calculation

import com.example.weshare.expense.Expense


class DebtCalculator {

    data class DebtRecord(val debtorId: String, val creditorId: String, val amountOwed: Double)

    /**
     * Calculates the debts for each member of a group.
     *
     * @param expenses List of all expenses for the group.
     * @return A list of debt records indicating who owes money to whom.
     */
    fun calculateDebts(expenses: List<Expense>): List<DebtRecord> {
        // Create a map to keep track of each person's balance
        val balances = mutableMapOf<String, Double>()

        // Calculate total expenses and individual balances
        expenses.forEach { expense ->
            val payerBalance = balances.getOrDefault(expense.payerId, 0.0) + expense.amount
            balances[expense.payerId] = payerBalance

            val splitAmount = expense.amount / expense.participants.size
            expense.participants.forEach { participantId ->
                val participantBalance = balances.getOrDefault(participantId, 0.0) - splitAmount
                balances[participantId] = participantBalance
            }
        }

        // Determine who owes what to whom
        val debts = mutableListOf<DebtRecord>()
        val creditors = balances.filter { it.value > 0 }
        val debtors = balances.filter { it.value < 0 }

        // This is a simplistic algorithm and might not scale well with a large number of transactions.
        // You may need a more complex algorithm that minimizes the number of transactions.

        debtors.forEach { (debtorId, debtorBalance) ->
            creditors.forEach { (creditorId, creditorBalance) ->
                if (debtorBalance < 0) {
                    val amountToSettle = Math.min(-debtorBalance, creditorBalance)
                    if (amountToSettle > 0) {
                        debts.add(DebtRecord(debtorId, creditorId, amountToSettle))
                        balances[debtorId] = debtorBalance + amountToSettle
                        balances[creditorId] = creditorBalance - amountToSettle
                    }
                }
            }
        }

        return debts
    }

    // Add other calculation-related methods if needed
}
