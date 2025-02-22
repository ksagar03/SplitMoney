package com.example.splitmoney

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@Composable
fun Navigation(viewModel: SplitMoneyViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                onGroupClick = { groupName -> navController.navigate("balanceView/${groupName}") },
                onAddExpenseClick = {
                    navController.navigate(("addExpense"))
                },
                onAddGroupClick = { navController.navigate("addGroup") })
        }

        composable("addExpense/{groupName}") { backStackEntry ->
            val groupName = backStackEntry.arguments?.getString("groupName")

            AddExpenseScreen(
                viewModel = viewModel,
                groupName = groupName ?: "",
                onExpenseAdded = { navController.popBackStack() })


        }

        composable("balanceView/{groupName}") { navBackStackEntry ->
            val groupName = navBackStackEntry.arguments?.getString("groupName")
            if (groupName != null) {
                BalanceSummaryScreen(
                    viewModel = viewModel,
                    groupName = groupName,
                    onBlockClick = {
                        navController.popBackStack()
                    }
                )
            }
        }

        composable("addGroup") {
            AddGroupScreen(viewModel = viewModel, onGroupAdded = { navController.popBackStack() })
        }

        composable("addExpense") {
            SelectGroupScreen(
                groups = viewModel.groups,
                onGroupSelected = { groupName ->
                    navController.navigate("addExpense/$groupName")

                },
                onCreateNewGroup = {
                    navController.navigate("addGroup")

                }
            )
        }


    }
}