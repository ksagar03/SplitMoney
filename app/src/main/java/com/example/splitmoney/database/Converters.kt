package com.example.splitmoney.database


import androidx.room.TypeConverter
import com.example.splitmoney.models.Expense
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType) ?: emptyList()
    }

    @TypeConverter
    fun fromExpenseList(value: List<Expense>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toExpenseList(value: String): List<Expense> {
        val listType = object : TypeToken<List<Expense>>() {}.type
        return gson.fromJson(value, listType) ?: emptyList()
    }
}








//
//import androidx.room.TypeConverter
//
//class Converters {
//    @TypeConverter
//    fun fromMembersList(members: List<String>): String {
//        return members.joinToString(",")
//    }
//
//    @TypeConverter
//    fun toMembersList(membersString: String): List<String> {
//        return if (membersString.isEmpty()) emptyList() else membersString.split(",")
//    }
//}