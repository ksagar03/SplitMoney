package com.example.splitmoney

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController



@Composable
fun HomeScreen(viewModel: SplitMoneyViewModel, onGroupClick: (Group) -> Unit , onAddExpenseClick: () -> Unit) {
    val groups = viewModel.groups

    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = {
            // need to add navigation to add group screen

        }, modifier = Modifier.fillMaxWidth()) {
            Text("Create New Group")

        }
        Spacer(modifier = Modifier.height(16.dp))

        if (groups.isEmpty()) {
            Text(
                "No Groups created yet",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding((16.dp))
            )

        } else {
            LazyColumn {
                items(groups) { group ->
                    GroupItem(group = group, onClick = { onGroupClick(group) })
                }
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
                text = "Members : ${group.members}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Text(
                text = "Total Expenses : ${group.totalExpenses}",
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


@Composable
fun GroupDetailsScreen(groupName: String) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = groupName ?: "Unknown Group",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)

        )
    }
}