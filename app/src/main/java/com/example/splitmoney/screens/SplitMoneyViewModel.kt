package com.example.splitmoney.screens


import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.splitmoney.database.ExpenseDao
import com.example.splitmoney.database.GroupDao
import com.example.splitmoney.database.PendingOperationDao
import com.example.splitmoney.firebase.SyncRepository
import com.example.splitmoney.models.Expense
import com.example.splitmoney.models.Group
import com.example.splitmoney.models.NetworkMonitor
import com.example.splitmoney.models.SyncStatus
import com.example.splitmoney.models.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel

class SplitMoneyViewModel @Inject constructor(
    private val groupDao: GroupDao,
    private val expenseDao: ExpenseDao,
    private val syncRepository: SyncRepository,
    private val networkMonitor: NetworkMonitor,
    private val pendingOperationDao: PendingOperationDao,
) : ViewModel() {
    private val _groups = MutableStateFlow<List<Group>>(emptyList())
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    private val _errorEvents = MutableSharedFlow<String>()

    private val _currentGroupExpenses = MutableStateFlow<List<Expense>>(emptyList())

    val groups: StateFlow<List<Group>> = _groups.asStateFlow()
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    val errorEvents: SharedFlow<String> = _errorEvents.asSharedFlow()
    val currentGroupExpenses: StateFlow<List<Expense>> = _currentGroupExpenses.asStateFlow()


//    val getUpdated_groups: StateFlow<List<Group>> = groupDao.getGroupsWithExpenses().map {
//        groupWithExpensesList ->
//        groupWithExpensesList.map { groupWithExpenses ->
//            groupWithExpenses.group.apply {
//                expenses = groupWithExpenses.expenses
//            }
//        }
//    }.stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.WhileSubscribed(1000),
//        initialValue = emptyList()
//    )


    private val _networkStatus = MutableStateFlow<Boolean?>(null)
    val networkStatus = _networkStatus.asStateFlow()

    private val _syncStatus = MutableStateFlow<SyncStatus>(SyncStatus.Idle)
    val syncStatus = _syncStatus.asStateFlow()

    private var currentGroupId: String? = null


    init {
        startNetworkMonitoring()
        monitorPendingOperations()
//        loadGroups()
        observeLocalGroups()
        performInitialSync()
    }

    private fun startNetworkMonitoring() {
        viewModelScope.launch {
            networkMonitor.isOnline.collect { isOnline ->
                _networkStatus.value = isOnline
                if (isOnline) {
                    manualSync()
                }
            }
        }

    }

    private fun monitorPendingOperations() {
        viewModelScope.launch {
            while (true) {
                val pendingCount = pendingOperationDao.getPendingOperationCount()
                _syncStatus.value = if (pendingCount > 0) {
                    SyncStatus.Pending(pendingCount)
                } else {
                    SyncStatus.Idle
                }
                delay(5000)
            }
        }

    }

    fun manualSync() {
        viewModelScope.launch {
            _syncStatus.value = SyncStatus.Syncing
            try {
                syncRepository.syncPendingOperations()
                _syncStatus.value = SyncStatus.Success
            } catch (e: Exception) {
                _syncStatus.value = SyncStatus.Error(e.message)
            }
        }
    }

//    private fun loadGroups() {
//        viewModelScope.launch {
//            _uiState.value = UiState.Loading
//            try {
//                groupDao.getGroupsWithExpenses().collect { groupWithExpensesList ->
//                    if (groupWithExpensesList.isNotEmpty()) {
//
//                        _groups.value = groupWithExpensesList.map { groupWithExpenses ->
//                            groupWithExpenses.group.copy().apply {
//
//                                expenses = groupWithExpenses.expenses
//
//
//                            }
//                        }
//                        _uiState.value = UiState.Success
//                    } else {
//                        _groups.value = emptyList()
//                        _uiState.value = UiState.Empty
//
//                    }
//
//                    if (syncRepository.isOnline()) {
//                        Log.d("ViewModel_DEBUG", "Calling syncGroups from loadGroups collect block.")
//                        val syncSuccess = syncRepository.syncGroups()
//
//                        if (syncSuccess) {
////                            reloadGroups()
////                            _uiState.value = UiState.Success
//                            Log.d("ViewModel_DEBUG", "syncGroups successful, local groups might be reloaded by Room Flow.")
//                        }
//                    }
//
//                }
//            } catch (e: Exception) {
//                _uiState.value = UiState.Error("Failed to load groups: ${e.message}")
//
//            }
//        }
//    }

    private fun observeLocalGroups(){
        viewModelScope.launch {

            groupDao.getGroupsWithExpenses().collect { groupWithExpensesList ->
                Log.d(
                    "ViewModel_DEBUG",
                    "observeLocalGroups: Data updated from Room. Size: ${groupWithExpensesList.size}"
                )
                if (groupWithExpensesList.isNotEmpty()) {
                    _groups.value = groupWithExpensesList.map { groupWithExpenses ->
                        groupWithExpenses.group.copy().apply {
                            expenses = groupWithExpenses.expenses
                        }
                    }
                    _uiState.value = UiState.Success
                }else {
                    _groups.value = emptyList()
                    _uiState.value = UiState.Empty

                }
            }
        }
    }

    private fun performInitialSync(){
        viewModelScope.launch {
            if(syncRepository.isOnline()){
                _uiState.value = UiState.Loading
                Log.d("ViewModel_DEBUG", "performInitialSync: Attempting to sync groups with Firestore.")
                try {
                    val syncSuccess = syncRepository.syncGroups()
                    if(syncSuccess){
                        Log.d("ViewModel_DEBUG", "performInitialSync: syncGroups successful. Room will update via observeLocalGroups.")

                    }else{
                        Log.w("ViewModel_DEBUG", "performInitialSync: syncGroups reported failure or was offline.")
                        if (_uiState.value == UiState.Loading) { // If still loading from this sync
                            _uiState.value = if (_groups.value.isEmpty()) UiState.Empty else UiState.Success
                        }
                    }
                }catch (e: Exception){
                    Log.e("ViewModel_DEBUG", "performInitialSync: Error during syncGroups: ${e.message}", e)
                    _errorEvents.emit("Failed to sync groups: ${e.message}")
                    if (_uiState.value == UiState.Loading) { // If still loading from this sync
                        _uiState.value = UiState.Error("Sync failed: ${e.message}")
                    }
                }
            }else {
                Log.d("ViewModel_DEBUG", "performInitialSync: Not online, skipping sync.")
                // Ensure UI state is not stuck in Loading if this was the only path to Success/Empty
                if (_uiState.value == UiState.Loading && _groups.value.isEmpty()){
                    _uiState.value = UiState.Empty
                } else if (_uiState.value == UiState.Loading && _groups.value.isNotEmpty()){
                    _uiState.value = UiState.Success
                }
            }
        }
    }

    private suspend fun reloadGroups() {
        val updatedGroups = groupDao.getGroupsWithExpenses().first()
        _groups.value = updatedGroups.map { it.group.copy().apply { expenses = it.expenses } }
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
                syncRepository.addGroupToFirebase(group)
//                Log.d("Group", "addGroup: $group")
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
//                val groupTouchDelete = _groups.value.find { it.id == groupId }
//                groupTouchDelete?.let {
//                    expenseDao.deleteExpensesForGroupID(groupId)
//                    groupDao.deleteGroup(it)
//                }
                syncRepository.deleteGroupFromFirebase(groupId)
            } catch (e: Exception) {
                _errorEvents.emit("Failed to delete group: ${e.message}")
            }
        }
    }

    fun addExpenseToGroup(groupId: String, expense: Expense, onExpenseAdded: () -> Unit) {

        viewModelScope.launch {
            try {
//                val expenseWithGroupId = expense.copy(groupId = groupId)
//                expenseDao.insertExpense(expenseWithGroupId)
                syncRepository.addExpenseToFirebase(expense)
                onExpenseAdded()

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

    fun editExpense(expenseID: String, newExpense: Expense, onExpenseEdited: () -> Unit) {
        viewModelScope.launch {
            try {
                syncRepository.updateExpenseInFirebase(newExpense.copy(id = expenseID))
//                expenseDao.updateExpense(newExpense.copy(id = expenseID))
                onExpenseEdited()
            } catch (e: Exception) {
                _errorEvents.emit("Failed to edit expense: ${e.message}")
            }
        }

    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {

            try {
                syncRepository.deleteExpenseFromFirebase(expense.id)
//                expenseDao.deleteExpense(expense)
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

    fun calculateBalancesV2(groupId: String): Map<String, Map<String, Double>> {
        val group = _groups.value.find { it.id == groupId } ?: return emptyMap()
        if (group.expenses.isEmpty()) return emptyMap()

        val totalAmount = group.expenses.sumOf { it.amount }
        val equalShare = totalAmount / group.members.size

        val balances = group.members.associateWith { member ->
            val paidAmount = group.expenses.filter { it.payer == member }.sumOf { it.amount }
            paidAmount - equalShare
        }

//        now i need to separate the creditors and debtors (i.e if the balance is positive then it is a creditor and vice versa)

        val creditors = balances.filter { it.value > 0 }.toMutableMap()
        val debtors = balances.filter { it.value < 0 }.mapValues { -it.value }.toMutableMap()

        val settlements = mutableMapOf<String, MutableMap<String, Double>>()

        group.members.forEach { member ->
            settlements[member] = mutableMapOf()
        }
        for ((debtor, debtAmt) in debtors) {
            var remainingDept = debtAmt
            for ((creditor, creditAmt) in creditors) {
                if (creditAmt == 0.0) continue
                val settlementAmount = minOf(remainingDept, creditAmt)
                if (settlementAmount > 0) {
                    settlements.getOrPut(debtor) { mutableMapOf() }[creditor] = settlementAmount

                    remainingDept -= settlementAmount
                    creditors[creditor] = creditAmt - settlementAmount
                    if (remainingDept == 0.0) break
                }
            }
        }
        return settlements

    }

    fun clearAllData() {
        viewModelScope.launch {

            try {
                groupDao.clearAllGroups()
                expenseDao.clearAllExpenses()

                _groups.value = emptyList()
                _uiState.value = UiState.Success
            } catch (e: Exception) {
                _errorEvents.emit("Failed to clear data: ${e.message}")
            }

        }

    }
}

