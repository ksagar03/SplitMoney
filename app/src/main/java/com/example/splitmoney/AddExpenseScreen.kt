package com.example.splitmoney

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AddExpenseScreen(
    viewModel: SplitMoneyViewModel,
    groupName: String,
    onExpenseAdded: () -> Unit
) {
    var expenseDescription by remember { mutableStateOf("") }
    var expenseAmount by remember { mutableStateOf("") }
    var selectedPayer by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        TextField(
            value = expenseDescription,
            onValueChange = { expenseDescription = it },
            label = { Text("User Expenses  Description") }
        )
        TextField(
            value = expenseAmount,
            onValueChange = { expenseAmount = it },
            label = { Text("User Expenses Amount") }
        )

//        val members = listOf("Alice", "Sagar", "Surya", "Maa")
//        var expanded by remember { mutableStateOf(false) }
//        Box(modifier = Modifier.fillMaxWidth()) {
//            Text(
//                text = selectedPayer.ifEmpty { "Select Payer" },
//                modifier = Modifier
//                    .clickable { expanded = true }
//                    .padding(16.dp)
//
//
//            )
//            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
//                members.forEach { member ->
//                    DropdownMenuItem(
//                        onClick = {
//                            selectedPayer = member
//                            expanded = false
//
//                        },
//                        text = { Text(member) }
//                    )
//                }
//
//            }
//        }
//
//        Button(
//            onClick = {
//                // Validate input before adding expense
//                if (expenseDescription.isNotBlank() && expenseAmount.isNotBlank() && selectedPayer.isNotBlank()) {
//                    val amount = expenseAmount.toDoubleOrNull()
//                    if (amount != null) {
//                        viewModel.addExpense(expenseDescription, amount, selectedPayer)
//                        // Clear fields after adding
//                        expenseDescription = ""
//                        expenseAmount = ""
//                        selectedPayer = ""
//                    } else {
//                        // Handle invalid amount (e.g., show an error message)
//                        errorMessage = "Invalid amount entered"
//                    }
//                } else {
//                    // Handle empty fields (e.g., show an error message)
//                    errorMessage = "Please fill all fields"
//                }
//            },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Add Expense")
//        }

        val group = viewModel.groups.find { it.name == groupName }
        val members = group?.members ?: emptyList()
        var expanded by remember { mutableStateOf(false) }
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = selectedPayer.ifEmpty { "Selected Payer" },
                modifier = Modifier
                    .clickable { expanded = true }
                    .padding(16.dp)

            )
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                members.forEach { member ->
                    DropdownMenuItem(
                        onClick = {
                            selectedPayer = member
                            expanded = false
                        },
                        text = { Text(member) }
                    )
                }
            }
        }

        Button(onClick = {
            if (expenseDescription.isEmpty() || expenseAmount.isEmpty() || selectedPayer.isEmpty()) {
                errorMessage = "Please fill all the fields"
            } else {
                try {
                    val amount = expenseAmount.toDouble()
                    val expense = Expense(expenseDescription, amount, selectedPayer)
                    viewModel.addExpenseToGroup(groupName, expense)
                    onExpenseAdded()
                } catch (e: NumberFormatException) {
                    errorMessage = "invalid amount. Please enter a valid number."
                }
            }
        }) {
            Text("Add Expense")
        }

    }


}