package com.example.splitmoney.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.splitmoney.screens.Group


@Database(entities = [Group::class, Expense::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase(){
    abstract fun groupDao(): GroupDao
    abstract fun expenseDao():ExpenseDao
}