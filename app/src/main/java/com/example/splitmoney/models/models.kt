package com.example.splitmoney.models


import androidx.compose.runtime.Immutable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.UUID
import kotlin.collections.emptyList


@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    var description: String,
    var amount: Double,
    var payer: String,
    var groupId: String?
)

@Immutable
@Entity(tableName = "groups")
data class  Group(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    var name: String,
    var members: List<String>,

){
    @Ignore
    var expenses: List<Expense> = emptyList()
}

data class GroupWithExpenses(
    @Embedded val group: Group,
    @Relation(
        parentColumn = "id",
        entityColumn = "groupId"
    )

    val expenses: List<Expense>
)

sealed class UiState {
    data object Loading: UiState()
    data object Success: UiState()
    data class Error(val message: String): UiState()
}