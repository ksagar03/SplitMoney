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
                onGroupClick = { group -> navController.navigate("balanceView/${group.name}") },
                onAddExpenseClick = {
                    navController.navigate(("addExpense"))
                })
        }

        composable("addExpense") {
            AddExpenseScreen(
                viewModel = viewModel,
                onExpenseAdded = { navController.popBackStack() })


        }

        composable("balanceView/{groupName}") {
            navBackStackEntry ->
            val groupName = navBackStackEntry.arguments?.getString("groupName")
            if (groupName != null) {
                BalanceSummaryScreen(
                    viewModel = viewModel,
                    groupName = groupName,
                    onBlockClick =  {
                        navController.popBackStack()
                    }
                )
            }
        }


    }
}