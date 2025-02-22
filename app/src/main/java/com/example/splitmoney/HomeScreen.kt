package com.example.splitmoney

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@Composable
fun HomeScreen(
    viewModel: SplitMoneyViewModel,
    onGroupClick: (String) -> Unit,
    onAddGroupClick: () -> Unit,
    onAddExpenseClick: () -> Unit
) {
    val groups = viewModel.groups


    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(
                onClick = onAddGroupClick  // need to add navigation to add group screen
                , modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create New Group")

            }



            if (groups.isEmpty()) {
                Text(
                    "No Groups created yet",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding((16.dp))
                )

            } else {
                LazyColumn(contentPadding = PaddingValues(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(groups) { group ->
                        GroupItem(group = group, onClick = { onGroupClick(group.name) })
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))


            Button(
                onClick = onAddExpenseClick,
                modifier = Modifier
                    .padding(16.dp)

            ) {
                Text("Add Expense")
            }
        }
    }

}


@Composable
fun GroupItem(group: Group, onClick: () -> Unit) {
//    Card(modifier = Modifier.fillMaxWidth().padding(8.dp).clickable { onClick() }, elevation= CardDefaults.cardElevation(defaultElevation = 4.dp) ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            Text(text = group.name, style = MaterialTheme.typography.headlineLarge)
//        }
//    }
    val totalAmount = group.expenses.sumOf { exp -> exp.amount }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = group.name,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Members : ${group.members.joinToString(", ")}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Total Expenses : ₹${"%.2f".format(totalAmount)}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

        }

    }


}

//@Composable
//fun SplitMoneyApp(viewModel: SplitMoneyViewModel) {
//    val navController = rememberNavController()
//    NavHost(navController, startDestination = "home") {
//        composable("home") {
//            HomeScreen(
//                viewModel = viewModel,
//                onGroupClick = { group -> navController.navigate("groupDetails/${group.name}") },
//                onAddExpenseClick =
//            )
//        }
//        composable("groupDetails/{groupName}".toString()) { backStackEntry ->
//            val groupName = backStackEntry.arguments?.getString("groupName")
//
//        }
//    }
//
//}

