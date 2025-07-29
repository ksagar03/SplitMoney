package com.example.splitmoney.screens


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.splitmoney.database.AppDatabase
import com.example.splitmoney.models.Expense
import com.example.splitmoney.models.Group
import com.example.splitmoney.models.UiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID


class SplitMoneyViewModel(private val database: AppDatabase) : ViewModel() {
    private val _groups = MutableStateFlow<List<Group>>(emptyList())
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    private val _errorEvents = MutableSharedFlow<String>()


    val groups: StateFlow<List<Group>> = _groups.asStateFlow()
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    val errorEvents: SharedFlow<String> = _errorEvents.asSharedFlow()


    init {
        loadGroups()
    }

    private fun loadGroups() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                database.groupDao().getGroupsWithExpenses().collect { groupWithExpensesList ->
                    val updateGroups = groupWithExpensesList.map { groupWithExpenses ->
                        Group(
                            id = groupWithExpenses.group.id,
                            name = groupWithExpenses.group.name,
                            members = groupWithExpenses.group.members,
                        ).apply {
                            expenses = groupWithExpenses.expenses
                        }

                    }
                    _groups.value = updateGroups
                    _uiState.value = UiState.Success

                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Failed to load groups: ${e.message}")

            }
        }
    }

    fun addGroup(name: String, members: List<String>) {
        viewModelScope.launch {
            try {
                val group = Group(
                    id = UUID.randomUUID().toString(),
                    name = name,
                    members = members
                )
                database.groupDao().insertGroup(group)
            } catch (e: Exception) {
                _errorEvents.emit("Failed to add group: ${e.message}")
            }


        }

    }


    fun editGroup(groupId: String, newName: String, newMembers: List<String>) {
        viewModelScope.launch {
            try {
                val existingGroup = _groups.value.find { it.id == groupId }
                existingGroup?.let {
                    val updatedGroup = it.copy(name = newName, members = newMembers)
                    database.groupDao().updateGroup(updatedGroup)
                }
            } catch (e: Exception) {
                _errorEvents.emit("Failed to edit group: ${e.message}")
            }


        }

    }

    fun deleteGroup(groupId: String) {
        viewModelScope.launch {
            try {
                val groupTouchDelete = _groups.value.find { it.id == groupId }
                groupTouchDelete?.let {
                    database.expenseDao().deleteExpensesForGroupID(groupId)
                    database.groupDao().deleteGroup(it)
                }
            } catch (e: Exception) {
                _errorEvents.emit("Failed to delete group: ${e.message}")
            }
        }
    }

    fun addExpenseToGroup(groupId: String, expense: Expense) {

        viewModelScope.launch {
            try {
                val expenseWithGroupId = expense.copy(groupId = groupId)
                database.expenseDao().insertExpense(expenseWithGroupId)
            } catch (e: Exception) {
                _errorEvents.emit("Failed to add expense: ${e.message}")
            }

        }
    }

    fun viewExpensesOfGroup(groupId: String): List<Expense> {
        return groups.value.find { it.id == groupId }?.expenses ?: emptyList()

    }

    fun editExpense(expenseID: String, newExpense: Expense) {
        viewModelScope.launch {
            try {
                database.expenseDao().updateExpense(newExpense.copy(id = expenseID))
            } catch (e: Exception) {
                _errorEvents.emit("Failed to edit expense: ${e.message}")
            }
        }

    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {

            try {
                database.expenseDao().deleteExpense(expense)
            } catch (e: Exception) {
                _errorEvents.emit("Failed to delete expense: ${e.message}")
            }
        }
    }

    fun calculateBalances(groupId: String): Map<String, Double> {
        val group = _groups.value.find { it.id == groupId } ?: return emptyMap()
        if (group.expenses.isEmpty()) return emptyMap()

        val totalAmount = group.expenses.sumOf { it.amount }
        val equalShare = totalAmount / group.members.size

        return group.members.associateWith { member ->
            val paidAmount = group.expenses.filter { it.payer == member }.sumOf { it.amount }
            paidAmount - equalShare
        }
    }
}

