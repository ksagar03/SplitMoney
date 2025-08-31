package com.example.splitmoney.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.splitmoney.models.PendingOperation


@Dao
interface PendingOperationDao {
    @Insert
    suspend fun insertOperation(operation: PendingOperation)

    @Update
    suspend fun updateOperation(operation: PendingOperation)

    @Delete
    suspend fun deleteOperation(id: Long)

    @Query("SELECT * FROM pending_operations ORDER BY createdAt ASC")
    suspend fun getPendingOperations(): List<PendingOperation>

    @Query("SELECT COUNT(*) FROM pending_operations")
    suspend fun getPendingOperationCount(): Int

}