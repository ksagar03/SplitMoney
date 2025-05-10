package com.example.splitmoney.screens

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.splitmoney.database.AppDatabase
import com.example.splitmoney.database.GroupEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

data class Expense(
    val id: String = UUID.randomUUID().toString(),
    var description: String,
    var amount: Double,
    var payer: String,
    var groupId: String,
)

//data class Group(val name: String, val members: List<String>, val totalExpenses: Double = 0.0)

//data class Exp(val description: String, val amount: Double, val payer: String)

data class Group(
    val id: String,
    val name: String,
    val members: List<String>,
    val expenses: List<Expense>,
)

class SplitMoneyViewModel(application: Application) : AndroidViewModel(application) {

    private val database: AppDatabase by lazy {
        Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "split-money-db"
        ).build()
    }
    private val firestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val currentUserId: String? get() = auth.currentUser?.uid

//-----------------------------------------------------------------------------

    private val _expenses = mutableStateListOf<Expense>()
    val expenses: List<Expense> get() = _expenses

    private val _members = mutableStateListOf<String>()
    val members: List<String> get() = _members

    private val _groups = mutableStateListOf<Group>()
    val groups: List<Group> get() = _groups

//---------------------------------------------------------------------------------------


    private val _groupsState = MutableStateFlow<List<Group>>(emptyList())
    val groupsState: StateFlow<List<Group>> = _groupsState.asStateFlow()

    private val _currentGroupState = MutableStateFlow<Group?>(null)
    val currentGroupState: StateFlow<Group?> = _currentGroupState.asStateFlow()

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()


    sealed class UiState {
        data object Loading : UiState()
        data object Success : UiState()
        data class Error(val message: String) : UiState()

    }

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading
                loadGroupsFromLocal()
                currentUserId?.let { syncWithFirebase() }
                _uiState.value = UiState.Success
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Failed to load data: ${e.message}")
            }
        }
    }

    private fun loadGroupsFromLocal() {
        viewModelScope.launch {
            database.groupDao().getGroupsWithExpenses().collect { groupsWithExpenses ->
                _groupsState.value = groupsWithExpenses.map { e ->
                    Group(
                        id = e.group.id,
                        name = e.group.name,
                        members = e.group.members,
                        expenses = expenses
                    )
                }
            }
        }
    }


    private fun syncWithFirebase() {
        currentUserId?.let { userId ->
            firestore.collection("users").document(userId).collection("groups")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        _uiState.value = UiState.Error("Failed to sync data: ${error.message}")
                        return@addSnapshotListener
                    }

                    snapshot?.documents?.forEach { doc ->
                        val group = doc.toObject(GroupEntity::class.java)?.copy(id = doc.id)
                        group?.let { syncGroupToLocal(it) }
                    }


                }
        }
    }

    private fun syncGroupToLocal(groupEntity: GroupEntity) {

        viewModelScope.launch {
            try {
                database.groupDao().insertGroup(groupEntity)

                currentUserId?.let { userId ->
                    firestore.collection("users").document(userId).collection("groups")
                        .document(groupEntity.id).collection("expenses").get()
                        .addOnSuccessListener { expDocs ->
                            expDocs.forEach { expDoc ->
                                val expense = expDoc.toObject(Expense::class.java)
                                    .copy(id = expDoc.id, groupId = groupEntity.id)
                                expense.let {
                                    viewModelScope.launch {
                                        database.expenseDao().insertExpense(it)
                                    }

                                }
                            }
                        }

                }

            } catch (e: Exception) {
                _uiState.value = UiState.Error("Sync Failed: ${e.message}")

            }
        }


    }


    fun addGroup(name: String, members: List<String>) {
        val groupId = UUID.randomUUID().toString()
        val groupEntity = GroupEntity(
            id = groupId,
            name = name,
            members = members,
        )
        val newGroup = Group(
            id = groupId,
            name = name,
            members = members,
            expenses = emptyList()
        )

        viewModelScope.launch {
            try {
                database.groupDao().insertGroup(groupEntity)

                _groupsState.value += newGroup

                currentUserId?.let { userId ->
                    firestore.collection("users").document(userId).collection("groups")
                        .document(groupId).set(groupEntity).addOnFailureListener { e ->
                            _uiState.value =
                                UiState.Error("Failed TO save group to Firebase: ${e.message} ")
                        }
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Failed to add group: ${e.message}")
                _groupsState.value = _groupsState.value.filter { it.id != groupId }
            }
        }
    }


    fun addExpenseToGroup(groupName: String, expense: Expense) {
        val group = _groups.find { it.name == groupName }
        group?.let {
            val updateExpense = it.expenses + expense
            val updatedGroup = it.copy(expenses = updateExpense)
            _groups[_groups.indexOf(it)] = updatedGroup
        }


    }
//..............

    fun viewExpensesOfGroup(groupName: String): List<Expense> {
        val group = _groups.find { it.name == groupName }
        return group?.expenses ?: emptyList()
    }

    fun selectGroup(groupID: String) {

        viewModelScope.launch {
            database.groupDao().getGroupWithExpenses(groupID).collect { groupWithExpenses ->
                groupWithExpenses.let { e ->
                    _currentGroupState.value = Group(
                        id = e.group.id,
                        name = e.group.name,
                        members = e.group.members,
                        expenses = e.expenses
                    )
                }

            }
        }
    }


    //    ...................................
    fun editGroup(oldName: String, newName: String, newMembers: List<String>) {
        val group = _groups.find { it.name == oldName }
        group?.let {
            val updatedGroup = it.copy(name = newName, members = newMembers)
            _groups[_groups.indexOf(it)] = updatedGroup
        }
    }


    fun updateGroup(groupID: String, newName: String, newMembers: List<String>) {

        viewModelScope.launch {
            try {

                val updateGroupEntity = GroupEntity(
                    id = groupID,
                    name = newName,
                    members = newMembers

                )
                database.groupDao().updateGroup(updateGroupEntity)

                currentUserId?.let { userId ->
                    firestore.collection("users").document(userId).collection("groups")
                        .document(groupID).set(updateGroupEntity)
                }

                _groupsState.value = _groupsState.value.map { group ->
                    if (group.id == groupID) {
                        group.copy(name = newName, members = newMembers)
                    } else {
                        group
                    }
                }

                _currentGroupState.value?.let { currentGroup ->
                    if (currentGroup.id == groupID) {
                        _currentGroupState.value =
                            currentGroup.copy(name = newName, members = newMembers)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Failed to update group: ${e.message}")

            }
        }
    }
    //    -----------------------------------

// ----------------------
//    fun deleteGroup(groupName: String) {
//        _groups.removeIf { it.name == groupName }
//
//    }

    fun deleteGroup(groupID: String) {
        viewModelScope.launch {
            try {
                database.groupDao()
                    .deleteGroup(GroupEntity(id = groupID, name = "", members = emptyList()))

                database.expenseDao().deleteExpensesForGroupID(groupID)

                currentUserId?.let { userId ->
                    firestore.collection("users").document(userId).collection("groups")
                        .document(groupID).delete()
                }

                _groupsState.value = _groupsState.value.filter {
                    it.id != groupID
                }

                if(_currentGroupState.value?.id == groupID){
                    _currentGroupState.value = null
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Failed to delete group: ${e.message}")

            }
        }

    }
//    ----------------------------------

//    Expense Operation

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

//    init {
//        _groups.addAll(
//            listOf(
//                Group(
//                    "Road Trip",
//                    listOf("alpha", "beta"),
//                    listOf(
//                        Expense(UUID.randomUUID().toString(), "Lunch", 5000.0, "Alice"),
//                        Expense(UUID.randomUUID().toString(), "Dinner", 7500.0, "Bob")
//                    )
//                ),
//                Group(
//                    "Dinner",
//                    listOf("Charlie", "gamma"),
//                    listOf(
//                        Expense(UUID.randomUUID().toString(), "Lunch", 5000.0, "Charlie"),
//                        Expense(UUID.randomUUID().toString(), "Dinner", 7500.0, "Dave")
//                    )
//                ),
//                Group("Movie", listOf("Eve", "Frank"))
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