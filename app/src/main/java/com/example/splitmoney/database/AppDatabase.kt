package com.example.splitmoney.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.splitmoney.models.Expense
import com.example.splitmoney.models.Group
import com.example.splitmoney.models.PendingOperation


@Database(entities = [Group::class, Expense::class, PendingOperation:: class], version = 5)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase(){
    abstract fun groupDao(): GroupDao
    abstract fun expenseDao():ExpenseDao

    abstract  fun pendingOperationDao(): PendingOperationDao
}