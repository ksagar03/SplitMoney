package com.example.splitmoney.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import androidx.room.TypeConverter
import androidx.room.Update
import com.example.splitmoney.models.Expense
import com.example.splitmoney.models.Group
import com.example.splitmoney.models.GroupWithExpenses
import kotlinx.coroutines.flow.Flow




@Dao
interface GroupDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(group: Group)

    @Update
    suspend fun updateGroup(group: Group)

    @Delete
    suspend fun deleteGroup(group: Group)

    @Transaction
    @Query("SELECT * FROM `groups`")
    fun getGroupsWithExpenses(): Flow<List<GroupWithExpenses>>
//    @Query("SELECT * FROM groups WHERE name = :name")
//    fun getGroupByName(name: String): Group?
//
//    @Query("SELECT * FROM groups WHERE id = :id")
//    suspend fun getGroupById(id: String): Group?


    @Transaction
    @Query("SELECT * FROM `groups` WHERE id = :groupId")
    suspend fun getGroupWithExpenses(groupId: String): GroupWithExpenses?

    @Query("DELETE FROM `groups`")
    suspend fun clearAllGroups()
}