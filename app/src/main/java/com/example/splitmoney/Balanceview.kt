package com.example.splitmoney

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.math.abs

@Composable
fun BalanceSummaryScreen(
    viewModel: SplitMoneyViewModel,
    groupName: String,
    onBlockClick: () -> Unit,
) {
//    val group = viewModel.groups.find { it.name == groupName }
//    val balances = group?.let {
//        viewModel.calculateSplit(
//            groupName,
//            members = group.members
//        )
//    } ?: emptyMap()
    val balances = viewModel.calculateBalances(groupName)


    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            "Balance Summary for $groupName",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        if (balances.isEmpty()) {
            Text("no Expenses added yet")
        } else {
            balances.forEach { (member, balance) ->
                Text(
                    "$member: ${if (balance >= 0) "gets" else "owes"} ₹${abs(balance)}",
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBlockClick) {
            Text("Back")
        }


    }
}