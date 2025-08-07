package com.example.splitmoney.screens


import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.example.splitmoney.models.Expense
import com.example.splitmoney.screens.components.MyOutlinedTextField
import com.example.splitmoney.screens.components.TextFieldParameters
import java.util.UUID


data class Edit(val isTrue: Boolean, val data: Expense)

@Composable
fun AddExpenseScreen(
    isEdit: Edit,
    viewModel: SplitMoneyViewModel,
    groupID: String,
    onExpenseAdded: () -> Unit,
) {
    var expenseDescription by remember { mutableStateOf(if (isEdit.isTrue) isEdit.data.description else "") }
    var expenseAmount by remember { mutableStateOf(if (isEdit.isTrue) isEdit.data.amount.toString() else "") }
    var selectedPayer by remember { mutableStateOf(if (isEdit.isTrue) isEdit.data.payer else "") }
    var expanded by remember { mutableStateOf(false) }

    var descriptionError by remember { mutableStateOf<String?>(null) }
    var amountError by remember { mutableStateOf<String?>(null) }
    var payerError by remember { mutableStateOf<String?>(null) }

    val errorEvent by viewModel.errorEvents.collectAsState(initial = "")

    val group = viewModel.getGroupInfo(groupID)
    val members = group?.members ?: emptyList()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(errorEvent) {
        snackbarHostState.showSnackbar(errorEvent)
    }

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
                    text = if (isEdit.isTrue) "Edit Expense for Group: ${group?.name}" else "Add Expense for Group: ${group?.name}",
                    modifier = Modifier.padding(bottom = 16.dp),
                    style = MaterialTheme.typography.headlineSmall,
                    color = colorResource(id = R.color.Dark_Theme_Text)
                )
                Spacer(modifier = Modifier.height(16.dp))
//                Expense field
                MyOutlinedTextField(
                    textFieldParameters = TextFieldParameters(
                        value = expenseDescription,
                        onValueChange = {
                            expenseDescription = it
                            descriptionError = null
                        },
                        label = "Expenses Description",
                        isError = descriptionError != null,
                        supportingText = descriptionError
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )
// Amount field
                MyOutlinedTextField(
                    textFieldParameters = TextFieldParameters(
                        label = "Expenses Amount", value = expenseAmount, onValueChange = {
                            expenseAmount = it
                            amountError = null
                        }, supportingText = descriptionError, isError = descriptionError != null
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(8.dp))


                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(text = selectedPayer.ifEmpty { "Select Payer" },
                        modifier = Modifier
                            .clickable { expanded = true }
                            .padding(16.dp)
                            .background(
                                color = colorResource(id = R.color.Dark_Theme_Secondary2),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(8.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (payerError != null) Color.Red else colorResource(id = R.color.Dark_Theme_Text)

                    )
                    if (payerError != null) {
                        Text(
                            text = payerError!!,
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .background(
                                colorResource(id = R.color.Dark_Theme_Primary),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .border(
                                shape = RoundedCornerShape(10.dp), color = Color.Gray, width = 1.dp
                            )
                    ) {
                        members.forEachIndexed { index, member ->

                            DropdownMenuItem(onClick = {
                                selectedPayer = member
                                payerError = null
                                expanded = false
                            }, text = {
                                Text(
                                    text = member,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = colorResource(id = R.color.Dark_Theme_Text)
                                )
                            })
                            if (index < members.size - 1) {
                                HorizontalDivider(thickness = 2.dp)
                            }
                        }
                    }
                }
//                AnimatedVisibility(visible = errorMessage != null) {
//                    errorMessage?.let {
//                        Text(
//                            text = it,
//                            color = Color.Red,
//                            modifier = Modifier.padding(bottom = 8.dp)
//                        )
//                    }
//
//                }
                Spacer(modifier = Modifier.height(16.dp))

//

                FloatingActionButton(
                    onClick = {
                        var isValid = true

                        if (expenseDescription.isEmpty()) {
                            descriptionError = "Please enter a description"
                            isValid = false
                        }
                        if (expenseAmount.isEmpty()) {
                            amountError = "Please enter an amount"
                            isValid = false
                        } else {
                            try {
                                expenseAmount.toDouble()
                            } catch (e: NumberFormatException) {
                                amountError = "Please enter a valid amount"
                                isValid = false
                            }
                        }
                        if (selectedPayer.isEmpty()) {
                            payerError = "Please select a payer"
                            isValid = false
                        }

                        if (isValid) {
                            val amount = expenseAmount.toDouble()
                            if (isEdit.isTrue) {
                                viewModel.editExpense(
                                    isEdit.data.id, Expense(
                                        isEdit.data.id,
                                        expenseDescription,
                                        amount,
                                        selectedPayer,
                                        group?.id ?: ""
                                    )

                                )
                            } else {
                                viewModel.addExpenseToGroup(
                                    group?.id ?: "", Expense(
                                        UUID.randomUUID().toString(),
                                        expenseDescription,
                                        amount,
                                        selectedPayer,
                                        group?.id ?: ""
                                    )
                                )
                            }
                            onExpenseAdded()
                        }
                    },
                    modifier = Modifier.align(Alignment.End),
                    containerColor = colorResource(id = R.color.Dark_Theme_Secondary),
                    contentColor = colorResource(id = R.color.Dark_Theme_Text)
                ) {
                    if (isEdit.isTrue) {
                        Text("Save")
                    } else {
                        Icon(Icons.Default.Add, contentDescription = "Add Expense")
                    }
                }
//
            }

        }
    }


}