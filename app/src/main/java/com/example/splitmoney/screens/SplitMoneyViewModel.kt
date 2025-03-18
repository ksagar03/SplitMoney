package com.example.splitmoney.screens

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

data class Expense(val description: String, val amount: Double, val payer: String)

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


    fun ViewExpensesOfGroup(groupName: String): List<Expense> {
        val group = _groups.find { it.name == groupName }
        return group?.expenses ?: emptyList()

    }

//    init {
//        _groups.addAll(
//            listOf(
//                Group("Road Trip", listOf("Alice", "Bob"), totalExpenses = 9000.0),
//                Group("Dinner", listOf("Charlie", "Dave"), totalExpenses = 300.0),
//                Group("Movie", listOf("Eve", "Frank"), totalExpenses = 5000.0)
//            )
//        )
//    }

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