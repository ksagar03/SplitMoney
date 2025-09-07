package com.example.splitmoney.firebase

import android.content.Context

import android.util.Log

import com.example.splitmoney.database.ExpenseDao
import com.example.splitmoney.database.GroupDao
import com.example.splitmoney.database.PendingOperationDao
import com.example.splitmoney.models.Expense
import com.example.splitmoney.models.Group
import com.example.splitmoney.models.NetworkMonitor
import com.example.splitmoney.models.PendingOperation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton


@Singleton

class SyncRepository @Inject constructor(
    private val groupDao: GroupDao,
    private val expenseDao: ExpenseDao,
    private val pendingOperationDao: PendingOperationDao,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val networkMonitor: NetworkMonitor,
    @ApplicationContext private val context: Context,
) {


    private val currentUser: String
        get() = auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")


    private val gson = Gson()

    suspend fun isOnline(): Boolean {
        return networkMonitor.isOnline.first()
    }


    suspend fun addGroupToFirebase(group: Group): String {
        return (if (isOnline()) {
            addGroupIfOnline(group)
        } else {
            queueOperation(
                operationType = "CREATE_GROUP",
                entityType = "Group",
                entityId = group.id,
                serializedData = gson.toJson(group)
            )
            group.id
        })

    }

    suspend fun addGroupIfOnline(group: Group): String {
        return try {
            val firebaseGroup = hashMapOf(
                "id" to group.id,
                "name" to group.name,
                "members" to group.members,
                "created By" to currentUser,
                "createdAt" to FieldValue.serverTimestamp(),
                "updatedAt" to FieldValue.serverTimestamp(),
            )

            val documentRef = firestore.collection("groups").document(group.id)
            documentRef.set(firebaseGroup).await()
            groupDao.insertGroup(group)
            group.id
        } catch (e: Exception) {
            Log.d("Firebase", "addGroupToFirebase: Failed to add group to Firebase $e ")
            throw SyncException("Failed to add group to Firebase", e)

        }

    }

    suspend fun addExpenseToFirebase(expense: Expense): String {

        return if (isOnline()) {
            addExpenseIfOnline(expense)
        } else {
            queueOperation(
                operationType = "CREATE_EXPENSE",
                entityType = "Expense",
                entityId = expense.id,
                serializedData = gson.toJson(expense)
            )
            expense.id
        }
    }

    suspend fun addExpenseIfOnline(expense: Expense): String {
        return try {
            val firebaseExpense = hashMapOf(
                "id" to expense.id,
                "description" to expense.description,
                "amount" to expense.amount,
                "payer" to expense.payer,
                "groupId" to expense.groupId,
                "createdAt" to FieldValue.serverTimestamp(),
                "updatedAt" to FieldValue.serverTimestamp()
            )
            val documentRef = firestore.collection("expenses").document(expense.id)
            documentRef.set(firebaseExpense).await()
            expenseDao.insertExpense(expense)
            expense.id
        } catch (e: Exception) {
            Log.d("Firebase", "addExpenseToFirebase: Failed to add expense to Firebase $e ")
            throw SyncException("Failed to add expense to Firebase", e)
        }
    }


    suspend fun updateGroupInFirebase(group: Group) {
        try {
            val updates = hashMapOf<String, Any>(
                "name" to group.name,
                "members" to group.members,
                "updatedAt" to FieldValue.serverTimestamp()
            )
            firestore.collection("groups").document(group.id).update(updates).await()
            groupDao.updateGroup(group)
        } catch (e: Exception) {
            Log.d("Firebase", "updateGroupInFirebase: Failed to update group in Firebase $e ")
            throw SyncException("Failed to update group in Firebase", e)

        }

    }

    suspend fun updateExpenseInFirebase(expense: Expense) {
        try {
            val updates = hashMapOf<String, Any>(
                "description" to expense.description,
                "amount" to expense.amount,
                "payer" to expense.payer,
                "updatedAt" to FieldValue.serverTimestamp()

            )
            firestore.collection("expenses").document(expense.id).update(updates).await()
            expenseDao.updateExpense(expense)

        } catch (e: Exception) {
            Log.d(
                "Firebase",
                "updateExpenseInFirebase: Failed to update expense in Firebase $e "
            )
            throw SyncException("Failed to update expense in Firebase", e)

        }
    }

    suspend fun deleteGroupFromFirebase(groupId: String) {
        try {
            val expenses =
                firestore.collection("expenses").whereEqualTo("groupId", groupId).get().await()
            expenses.forEach { doc -> doc.reference.delete().await() }

            firestore.collection("groups").document(groupId).delete().await()

            groupDao.deleteGroupById(groupId)
            expenseDao.deleteExpensesForGroupID(groupId)
        } catch (e: Exception) {
            Log.d(
                "Firebase",
                "deleteGroupFromFirebase: Failed to delete group from Firebase $e "
            )
            throw SyncException("Failed to delete group from Firebase", e)
        }

    }


    suspend fun deleteExpenseFromFirebase(expenseId: String) {
        try {
            firestore.collection("expenses").document(expenseId).delete().await()
            expenseDao.deleteExpenseViaID(expenseId)

        } catch (e: Exception) {
            Log.d(
                "Firebase",
                "deleteExpenseFromFirebase: Failed to delete expense from Firebase $e "
            )
            throw SyncException("Failed to delete expense from Firebase", e)
        }


    }

    suspend fun syncGroups(): Boolean {

        if (!isOnline()) return false


        try {
            Log.d("UserIDD", "syncGroups: $currentUser")
            val groups =
                firestore.collection("groups").whereEqualTo("created By", currentUser).get().await()
//            Log.d("FetchedG", "groups: ${groups?.size()}")
            if (groups == null) {
                return true
            }

            groups.forEach { doc ->
                Log.d("ddd", "doc: ${doc.id} && data:: ${doc.data}")
                val firebaseGroup = doc.toObject(Group::class.java)
                Log.d(
                    "SYNC_DEBUG",
                    "Deserialized -> ID: ${firebaseGroup.id}, Name: ${firebaseGroup.name}, Members: ${firebaseGroup.members}"
                )
                syncExpenses(firebaseGroup.id)
                groupDao.insertGroup(firebaseGroup)
            }

            return true
        } catch (e: Exception) {
            throw SyncException("Failed to sync groups", e)
        }


    }

    suspend fun syncExpenses(groupId: String) {
        val expense =
            firestore.collection("expenses").whereEqualTo("groupId", groupId).get().await()
        Log.d("FETCHED_EXP", "$expense")
        expense.forEach { doc ->
            val firebaseExpense = doc.toObject(Expense::class.java)
            expenseDao.insertExpense(firebaseExpense)
        }
    }

    private suspend fun queueOperation(
        operationType: String,
        entityType: String,
        entityId: String,
        serializedData: String,
    ) {
        val operation = PendingOperation(
            operationType = operationType,
            entityType = entityType,
            entityId = entityId,
            serializedData = serializedData,
            retryCount = 0,
            lastAttempt = 0
        )

        pendingOperationDao.insertOperation(operation)

    }


    fun startNetworkMonitoring(scope: CoroutineScope) {
        scope.launch {
            networkMonitor.isOnline.collect { isOnline ->
                if (isOnline) {
                    syncPendingOperations()
                }
            }
        }

    }

    suspend fun syncPendingOperations() {
        if (!isOnline()) return
        val pendingOps = pendingOperationDao.getPendingOperations()

        for (ops in pendingOps) {
            try {
                when (ops.operationType) {
                    "CREATE_GROUP" -> {
                        val group = gson.fromJson(ops.serializedData, Group::class.java)
                        addGroupIfOnline(group)
                    }

                    "CREATE_EXPENSE" -> {
                        val expense = gson.fromJson(ops.serializedData, Expense::class.java)

                    }

                    "UPDATE_GROUP" -> {
                        val group = gson.fromJson(ops.serializedData, Group::class.java)
                        updateGroupInFirebase(group)
                    }

                    "UPDATE_EXPENSE" -> {
                        val expense = gson.fromJson(ops.serializedData, Expense::class.java)
                        updateExpenseInFirebase(expense)
                    }

                    "DELETE_GROUP" -> deleteGroupFromFirebase(ops.entityId)
                    "DELETE_EXPENSE" -> deleteExpenseFromFirebase(ops.entityId)
                }
                pendingOperationDao.deleteOperation(ops)
            } catch (e: Exception) {
                ops.retryCount++
                ops.lastAttempt = System.currentTimeMillis()
                pendingOperationDao.updateOperation(ops)

                if (ops.retryCount >= 5) {
                    pendingOperationDao.deleteOperation(ops)
//                    if it is not synced then will delete the operation from the database also need to notify the user regarding this failure

                }

            }
        }
    }

//    fun registerNetworkCallback() {
//        val networkCallback = object : ConnectivityManager.NetworkCallback() {
//            override fun onAvailable(network: Network) {
//               application.applicationScope.launch {
//                   syncPendingOperations()
//               }
//            }
//
//            override fun onLost(network: Network){
//                Log.d("Network", "connection lost ")
//            }
//        }
//
//        connectivityManager.registerNetworkCallback(
//            NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build(),
//            networkCallback
//        )
//    }


}


class SyncException(message: String, cause: Throwable? = null) : Exception(message, cause)









