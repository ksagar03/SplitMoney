package com.example.splitmoney.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.TypeConverter
import androidx.room.Update
import com.example.splitmoney.screens.Expense

@Entity(tableName = "groups")
data class Group(
    @PrimaryKey
    val name: String,
    val members: List<String>,
    val expense: List<Expense> = emptyList(),

    )

// Type Convertors
class Converters {
    @TypeConverter
    fun fromMembersList(members: List<String>): String {
        return members.joinToString(",")
    }

    @TypeConverter
    fun toMembersList(membersString: String): List<String> {
        return if (membersString.isEmpty()) emptyList() else membersString.split(",")
    }
}


@Dao
interface GroupsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(group: Group)

    @Update
    suspend fun updateGroup(group: Group)

    @Delete
    suspend fun deleteGroup(group: Group)

    @Query("SELECT * FROM groups")
    fun getAllGroups(): List<Group>

    @Query("SELECT * FROM groups WHERE name = :name")
    fun getGroupByName(name: String): Group?

}