package com.example.splitmoney.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.splitmoney.models.Expense
import kotlinx.coroutines.flow.Flow


@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense:Expense)

    @Update
    suspend fun updateExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)


    @Query("SELECT * FROM expenses WHERE groupId = :groupId")
    fun getExpensesForGroup(groupId: String): Flow<List<Expense>>


    //    To Delete All the expenses related to a group in the database when group is deleted
    @Query("DELETE FROM expenses WHERE groupId = :groupId")
    suspend fun deleteExpensesForGroupID(groupId: String)

    @Query("DELETE FROM expenses WHERE  id = :expenseId")
    suspend fun deleteExpenseViaID(expenseId: String)

    @Query("DELETE FROM expenses")
    fun clearAllExpenses()


}
//
//
//
//
