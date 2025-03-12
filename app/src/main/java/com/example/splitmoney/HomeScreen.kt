package com.example.splitmoney

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.splitmoney.header.Header
import com.example.splitmoney.signuporlogin.AuthViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    viewModel: SplitMoneyViewModel,
    authViewModel: AuthViewModel,
    onGroupClick: (String) -> Unit,
    onAddGroupClick: () -> Unit,
    onAddExpenseClick: () -> Unit,
    onLogout: () -> Unit,

    ) {
    val groups = viewModel.groups

    Scaffold(topBar = {
        Header(
            onLogoutClick = { authViewModel.logout() },
        )
    }) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(top = 60.dp)) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {


                if (groups.isEmpty()) {
                    Text(
                        "No Groups created yet",
                        style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray),
                        modifier = Modifier.padding((16.dp))
                    )

                } else {
                    println("Groups are not empty, displaying LazyColumn")
                    LazyColumn(
                        contentPadding = PaddingValues(bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(groups) { group ->
                            println("Displaying group: ${group.name}")
                            GroupItem(group = group, onClick = { onGroupClick(group.name) })
                        }
                    }
                }

                Button(
                    onClick = onAddGroupClick,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Create New Group")

                }

            }


//        Button(
//            onClick = onAddExpenseClick,
//            modifier = Modifier
//                .padding(20.dp)
//                .align(Alignment.BottomEnd)
//
//        ) {
//            Text("Add Expense")
//        }
            FloatingActionButton(
                onClick = onAddExpenseClick,
                modifier = Modifier
                    .padding(30.dp)
                    .width(150.dp)
                    .align(Alignment.BottomEnd),
                containerColor = MaterialTheme.colorScheme.secondary


            ) {
                Row(

                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "Add Expense"
                    )
                    Text("Add Expense")
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
    val totalAmount = group.expenses.sumOf { exp -> exp.amount }
    var isPressed by remember { mutableStateOf(false) }
    val animatedElevation by animateDpAsState(
        targetValue = if (isPressed) 12.dp else 6.dp,
        animationSpec = tween(durationMillis = 200), label = ""
    )
    val animatedAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 500), label = ""
    )
    val animatedIconScale by animateFloatAsState(
        targetValue = if (isPressed) 1.2f else 1f,
        animationSpec = tween(durationMillis = 150), label = ""
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                isPressed = !isPressed
                onClick()
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = animatedElevation)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = group.name,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 8.dp),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Members : ${group.members.joinToString(", ")}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            AnimatedVisibility(visible = animatedAlpha == 1f) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "₹",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.scale(animatedIconScale)
                    )
                    Text(
                        text = " : ${"%.2f".format(totalAmount)}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )
                }

            }


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

