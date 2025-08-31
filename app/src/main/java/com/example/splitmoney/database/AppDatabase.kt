package com.example.splitmoney.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.splitmoney.models.Expense
import com.example.splitmoney.models.Group


@Database(entities = [Group::class, Expense::class], version = 3)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase(){
    abstract fun groupDao(): GroupDao
    abstract fun expenseDao():ExpenseDao

    abstract  fun pendingOperationDao(): PendingOperationDao
}