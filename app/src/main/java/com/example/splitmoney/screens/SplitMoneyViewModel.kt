package com.example.splitmoney.screens


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.splitmoney.database.ExpenseDao
import com.example.splitmoney.database.GroupDao
import com.example.splitmoney.models.Expense
import com.example.splitmoney.models.Group
import com.example.splitmoney.models.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel

class SplitMoneyViewModel @Inject constructor(
    private val groupDao: GroupDao,
    private val expenseDao: ExpenseDao,
) : ViewModel() {
    private val _groups = MutableStateFlow<List<Group>>(emptyList())
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    private val _errorEvents = MutableSharedFlow<String>()

    private val _currentGroupExpenses = MutableStateFlow<List<Expense>>(emptyList())

    val groups: StateFlow<List<Group>> = _groups.asStateFlow()
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    val errorEvents: SharedFlow<String> = _errorEvents.asSharedFlow()
    val currentGroupExpenses: StateFlow<List<Expense>> = _currentGroupExpenses.asStateFlow()

    private var currentGroupId: String? = null


    init {
        loadGroups()
    }

    private fun loadGroups() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                groupDao.getGroupsWithExpenses().collect { groupWithExpensesList ->
                    _groups.value = groupWithExpensesList.map { groupWithExpenses ->
                        Group(
                            id = groupWithExpenses.group.id,
                            name = groupWithExpenses.group.name,
                            members = groupWithExpenses.group.members,
                        ).apply {
                            if (groupWithExpenses.group.id != currentGroupId) {
                                expenses = groupWithExpenses.expenses
                            }

                        }

                    }
                    _uiState.value = UiState.Success

                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Failed to load groups: ${e.message}")

            }
        }
    }

//    fun refreshGroupExpenses(groupID: String){
//        viewModelScope.launch {
//            val updatedExpense = expenseDao.getExpensesForGroup(groupID)
//            _groups.value = _groups.value.map {
//                group ->
//                if(group.id == groupID){
//                    group.apply { expenses = updatedExpense }
//                }else{
//                    group
//                }
//            }
//        }
//
//    }


    fun setCurrentGroup(groupId: String) {
        currentGroupId = groupId
        viewModelScope.launch {
            expenseDao.getExpensesForGroup(groupId).collect { expenses ->
                _currentGroupExpenses.value = expenses
                _groups.value = _groups.value.map { group ->
                    if (group.id == groupId) {
                        group.apply { this.expenses = expenses }
                    } else {
                        group
                    }
                }
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
                groupDao.insertGroup(group)
                Log.d("Group", "addGroup: $group")
            } catch (e: Exception) {
                _errorEvents.emit("Failed to add group: ${e.message}")
                Log.d("Error_ADD", "addGroup: ${e.message}")
            }


        }

    }


    fun editGroup(groupId: String, newName: String, newMembers: List<String>) {
        viewModelScope.launch {
            try {
                val existingGroup = _groups.value.find { it.id == groupId }
                existingGroup?.let {
                    val updatedGroup = it.copy(name = newName, members = newMembers)
                    groupDao.updateGroup(updatedGroup)
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
                    expenseDao.deleteExpensesForGroupID(groupId)
                    groupDao.deleteGroup(it)
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
                expenseDao.insertExpense(expenseWithGroupId)

            } catch (e: Exception) {
                _errorEvents.emit("Failed to add expense: ${e.message}")
                Log.d("Error Exp", "addExpenseToGroup: ${e.message}")
            }

        }
    }

    fun getGroupInfo(groupId: String): Group? {
        return groups.value.find { it.id == groupId }
    }

    fun viewExpensesOfGroup(groupId: String): List<Expense> {
        return groups.value.find { it.id == groupId }?.expenses ?: emptyList()

    }

    fun editExpense(expenseID: String, newExpense: Expense) {
        viewModelScope.launch {
            try {
                expenseDao.updateExpense(newExpense.copy(id = expenseID))
            } catch (e: Exception) {
                _errorEvents.emit("Failed to edit expense: ${e.message}")
            }
        }

    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {

            try {
                expenseDao.deleteExpense(expense)
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

