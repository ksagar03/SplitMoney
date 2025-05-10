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
import androidx.room.TypeConverter
import androidx.room.Update
import com.example.splitmoney.screens.Expense
import com.example.splitmoney.screens.Group
import kotlinx.coroutines.flow.Flow
import java.util.UUID


data class GroupWithExpenses(
    @Embedded val group: GroupEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "groupId"
    )
    val expenses: List<Expense>
)


@Entity(tableName = "groups")
data class GroupEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val members: List<String>,


    ){
    @Ignore
    var expenses: List<Expense> = emptyList()

    constructor(name: String, members: List<String>, expenses: List<Expense>) : this(UUID.randomUUID().toString(), name, members){
        this.expenses = expenses
    }
}

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
interface GroupDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(group: GroupEntity)

    @Update
    suspend fun updateGroup(group: GroupEntity)

    @Delete
    suspend fun deleteGroup(group: GroupEntity)

    @Query("SELECT * FROM groups")
    fun getGroupsWithExpenses(): Flow<List<GroupWithExpenses>>

//    @Query("SELECT * FROM groups WHERE name = :name")
//    fun getGroupByName(name: String): Group?
//
//    @Query("SELECT * FROM groups WHERE id = :id")
//    suspend fun getGroupById(id: String): Group?


    @Query("SELECT * FROM groups WHERE id = :groupId")
    fun getGroupWithExpenses(groupId: String): Flow<GroupWithExpenses>


}