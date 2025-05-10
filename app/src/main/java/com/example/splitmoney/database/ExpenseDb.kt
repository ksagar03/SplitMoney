package com.example.splitmoney.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import java.util.UUID

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    var description: String,
    var amount: Double,
    var payer: String,
    var groupId: String,
)


@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: com.example.splitmoney.screens.Expense)

    @Update
    suspend fun updateExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("SELECT * FROM expenses WHERE groupId = :groupId")
    fun getExpensesForGroup(groupId: String): List<Expense>

    @Query("DELETE FROM expenses WHERE groupId = :groupId")
    suspend fun deleteExpensesForGroupID(groupId: String)
}




