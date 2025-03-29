package com.example.splitmoney.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.splitmoney.R
import com.example.splitmoney.screens.components.MyOutlinedTextField
import com.example.splitmoney.screens.components.textFieldParameters
import com.example.splitmoney.signuporlogin.components.DividerComponent
import java.util.UUID


data class Edit(val isTrue: Boolean, val data: Expense)

@Composable
fun AddExpenseScreen(
    isEdit: Edit,
    viewModel: SplitMoneyViewModel,
    groupName: String,
    onExpenseAdded: () -> Unit,
) {
    var expenseDescription by remember { mutableStateOf(if (isEdit.isTrue) isEdit.data.description else "") }
    var expenseAmount by remember { mutableStateOf(if (isEdit.isTrue) isEdit.data.amount.toString() else "") }
    var selectedPayer by remember { mutableStateOf(if (isEdit.isTrue) isEdit.data.payer else "") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) }

    val gradient = Brush.verticalGradient(
        colors = listOf(
            colorResource(id = R.color.Dark_Theme_Primary),
            colorResource(id = R.color.Dark_Theme_Secondary)

        )
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(16.dp)
                .animateContentSize(),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(id = R.color.Dark_Theme),

                )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text(
                    text = if (isEdit.isTrue) "Edit Expense for Group: $groupName" else "Add Expense for Group: $groupName",
                    modifier = Modifier.padding(bottom = 16.dp),
                    style = MaterialTheme.typography.headlineSmall,
                    color = colorResource(id = R.color.Dark_Theme_Text)
                )

                MyOutlinedTextField(
                    textFieldParameters = textFieldParameters(
                        value = expenseDescription,
                        onValueChange = { expenseDescription = it },
                        label = "Expenses Description"
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )

                MyOutlinedTextField(
                    textFieldParameters = textFieldParameters(
                        value = expenseAmount,
                        onValueChange = { expenseAmount = it },
                        label = "Expenses Amount"
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(8.dp))
                val group = viewModel.groups.find { it.name == groupName }
                val members = group?.members ?: emptyList()

                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = selectedPayer.ifEmpty { "Select Payer" },
                        modifier = Modifier
                            .clickable { expanded = true }
                            .padding(16.dp)
                            .background(
                                color = colorResource(id = R.color.Dark_Theme_Secondary2),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(8.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorResource(id = R.color.Dark_Theme_Text)

                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(colorResource(id = R.color.Dark_Theme_Primary))
                    ) {
                        members.forEach { member ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedPayer = member
                                    expanded = false
                                },
                                text = {
                                    Text(
                                        text = member,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = colorResource(id = R.color.Dark_Theme_Text)
                                    )
                                    DividerComponent()
                                }
                            )
                        }
                    }
                }
                AnimatedVisibility(visible = errorMessage != null) {
                    errorMessage?.let {
                        Text(
                            text = it,
                            color = Color.Red,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                }
                Spacer(modifier = Modifier.height(16.dp))

                FloatingActionButton(
                    onClick = {
                        if (expenseDescription.isEmpty() || expenseAmount.isEmpty() || selectedPayer.isEmpty()) {
                            errorMessage = "Please fill all the fields"
                        } else if(isEdit.isTrue){
                            val amount = expenseAmount.toDouble()
                            val expense = Expense(isEdit.data.id,expenseDescription, amount, selectedPayer)
                            viewModel.editExpense(groupName, isEdit.data.id, expense)
                            onExpenseAdded()
                        } else {
                            try {
                                val amount = expenseAmount.toDouble()
                                val expense = Expense(UUID.randomUUID().toString(),expenseDescription, amount, selectedPayer)
                                viewModel.addExpenseToGroup(groupName, expense)
                                onExpenseAdded()
                            } catch (e: NumberFormatException) {
                                errorMessage = "invalid amount. Please enter a valid number."
                            }
                        }
                    },
                    modifier = Modifier.align(Alignment.End),
                    containerColor = colorResource(id = R.color.Dark_Theme_Secondary),
                    contentColor = colorResource(id = R.color.Dark_Theme_Text)
                ) {
                    if (isEdit.isTrue) Text(
                        "Save",
                        color = colorResource(id = R.color.Dark_Theme_Text),
                        style = MaterialTheme.typography.headlineSmall
                    ) else Icon(Icons.Default.Add, contentDescription = "Add Expense")
                }

            }

        }
    }


}