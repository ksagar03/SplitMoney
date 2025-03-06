package com.example.splitmoney

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.splitmoney.signuporlogin.AuthScreen
import com.example.splitmoney.signuporlogin.AuthStateInfo
import com.example.splitmoney.signuporlogin.AuthViewModel


@Composable
fun Navigation(viewModel: SplitMoneyViewModel, authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    val authStateInfo = authViewModel.authState.collectAsState()

    NavHost(navController = navController, startDestination = "auth") {

        composable("auth") {
            when (authStateInfo.value) {
                AuthStateInfo.Loading -> {
                    CircularProgressIndicator()
                }

                AuthStateInfo.Authenticated -> {
                    navController.navigate("home") {
                        popUpTo("auth") { inclusive = true }
                    }
                }

                AuthStateInfo.Unauthenticated -> {
                    AuthScreen(viewModel = authViewModel, onSuccess = {
                        navController.navigate("home") {
                            popUpTo("auth") { inclusive = true }
                        }
                    })
                }
            }
        }

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