package com.example.splitmoney.screens

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import java.util.UUID

data class Expense(
    val id: String = UUID.randomUUID().toString(),
    var description: String,
    var amount: Double,
    var payer: String,
)

//data class Group(val name: String, val members: List<String>, val totalExpenses: Double = 0.0)

//data class Exp(val description: String, val amount: Double, val payer: String)

data class Group(
    val name: String,
    val members: List<String> = emptyList(),
    val expenses: List<Expense> = emptyList(),
)

class SplitMoneyViewModel : ViewModel() {
    private val _expenses = mutableStateListOf<Expense>()
    val expenses: List<Expense> get() = _expenses

    private val _members = mutableStateListOf<String>()
    val members: List<String> get() = _members

    private val _groups = mutableStateListOf<Group>()
    val groups: List<Group> get() = _groups

    fun addGroup(name: String, members: List<String>) {
        _groups.add(Group(name, members))
    }

    fun addExpenseToGroup(groupName: String, expense: Expense) {
        val group = _groups.find { it.name == groupName }
        group?.let {
            val updateExpense = it.expenses + expense
            val updatedGroup = it.copy(expenses = updateExpense)
            _groups[_groups.indexOf(it)] = updatedGroup
        }


    }


    fun viewExpensesOfGroup(groupName: String): List<Expense> {
        val group = _groups.find { it.name == groupName }
        return group?.expenses ?: emptyList()
    }

    fun editGroup(oldName: String, newName: String, newMembers: List<String>) {
        val group = _groups.find { it.name == oldName }
        group?.let {
            val updatedGroup = it.copy(name = newName, members = newMembers)
            _groups[_groups.indexOf(it)] = updatedGroup
        }
    }

    fun deleteGroup(groupName: String) {
        _groups.removeIf { it.name == groupName }

    }

    fun editExpense(groupName: String, expenseID: String, newExpense: Expense) {
        val group = _groups.find { it.name == groupName }
//        group?.let {
//            val updatedExpense = it.expenses.toMutableList()
//            val index = updatedExpense.indexOf(oldExpense)
//            if (index != -1) {
//                updatedExpense[index] = newExpense
//                val updatedGroup = it.copy(expenses = updatedExpense)
//                _groups[_groups.indexOf(it)] = updatedGroup
//            }
//        }
        group?.expenses?.find { it.id == expenseID }?.let { expense ->
            expense.description = newExpense.description
            expense.amount = newExpense.amount
            expense.payer = newExpense.payer
        }
    }

    fun deleteExpense(groupName: String, expense: Expense) {
        val group = _groups.find { it.name == groupName }
        group?.let {
            val updatedExpense = it.expenses.toMutableList()
            updatedExpense.remove(expense)
            val updatedGroup = it.copy(expenses = updatedExpense)
            _groups[_groups.indexOf(it)] = updatedGroup
        }
    }

    init {
        _groups.addAll(
            listOf(
                Group(
                    "Road Trip",
                    listOf("alpha", "beta"),
                    listOf(
                        Expense(UUID.randomUUID().toString(), "Lunch", 5000.0, "Alice"),
                        Expense(UUID.randomUUID().toString(), "Dinner", 7500.0, "Bob")
                    )
                ),
                Group(
                    "Dinner",
                    listOf("Charlie", "gamma"),
                    listOf(
                        Expense(UUID.randomUUID().toString(), "Lunch", 5000.0, "Charlie"),
                        Expense(UUID.randomUUID().toString(), "Dinner", 7500.0, "Dave")
                    )
                ),
                Group("Movie", listOf("Eve", "Frank"))
            )
        )
    }

//    fun addExpense(description: String, amount: Double, payer: String) {
//        _expenses.add(Expense(description, amount, payer))
//    }
//
//    fun addMember(member: String) {
//        _members.add(member)
//    }


//    fun calculateSplit(expense: String, members: List<String>): Map<String, Double> {
//        val totalAmount = expense.sumOf { it.amount }
//        val equalShare = totalAmount / members.size
//        val balances = mutableMapOf<String, Double>()
//
//        members.forEach { member ->
//            val paidAmount = expenses.filter { it.payer == member }.sumOf { it.amount }
//            balances[member] = paidAmount - equalShare
//        }
//        return balances
//    }
//

    // Updated Calculate Expenses

    fun calculateBalances(groupName: String): Map<String, Double> {

        val group = _groups.find { it.name == groupName } ?: return emptyMap()
        if (group.expenses.isEmpty()) return emptyMap()
        val totalAmount = group.expenses.sumOf { expenses -> expenses.amount }
        val equalShare = totalAmount / group.members.size
        return group.members.associateWith { member ->
            val paidAmount = group.expenses.filter { expense -> expense.payer == member }
                .sumOf { expense -> expense.amount }
            paidAmount - equalShare
        }

    }
}