package com.example.splitmoney.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.splitmoney.signuporlogin.AuthScreen
import com.example.splitmoney.signuporlogin.AuthStateInfo
import com.example.splitmoney.signuporlogin.AuthViewModel
import com.example.splitmoney.signuporlogin.components.Progressbar


@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun Navigation(viewModel: SplitMoneyViewModel, authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    val authStateInfo = authViewModel.authState.collectAsState()

    LaunchedEffect(authStateInfo.value) {
        when (authStateInfo.value) {
            AuthStateInfo.Authenticated -> {
                navController.navigate("home") {
                    popUpTo("auth") { inclusive = true }

                }
            }

            AuthStateInfo.Unauthenticated -> {
                navController.navigate("auth") {
                    popUpTo("auth") { inclusive = true }

                }
            }

            AuthStateInfo.Loading -> {
//                We can't pass the composable function to the LaunchedEffect

            }

        }
    }

    NavHost(navController = navController, startDestination = "auth") {
        composable("auth") {
            when (authStateInfo.value) {
                AuthStateInfo.Loading -> {
                    Progressbar()
                }

                AuthStateInfo.Authenticated -> {

                }

                AuthStateInfo.Unauthenticated -> {
                    AuthScreen(
                        viewModel = authViewModel,
                        onSuccess = {
                        }
                    )
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
                onAddGroupClick = { navController.navigate("addGroup") },
                authViewModel = authViewModel,
                onLogout = {
                    navController.navigate("auth") {
                        popUpTo("auth") {
                            inclusive = true
                        }
                    }
                },
                onEditGroupClick = { groupName -> navController.navigate("editGroup/${groupName}") },
                onDeleteGroupClick = { groupName -> viewModel.deleteGroup(groupName.toString()) }
            )
        }

        composable("addExpense/{groupName}") { backStackEntry ->
            val groupName = backStackEntry.arguments?.getString("groupName")

            AddExpenseScreen(
                viewModel = viewModel,
                groupName = groupName ?: "",
                onExpenseAdded = { navController.popBackStack() },
                isEdit = Edit(false, Expense("","", 0.0, ""))
                )


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


        composable("editGroup/{groupName}") { navBackStackEntry ->
            val groupName = navBackStackEntry.arguments?.getString("groupName")
            val group = viewModel.groups.find { it.name == groupName }
            if (group != null) {
                EditGroupScreen(
                    group = group,
                    onSave = { newName, newMembers ->
                        if (groupName != null) {
                            viewModel.editGroup(groupName, newName, newMembers)
                        }
                        navController.popBackStack()
                    },
                    onCancel = { navController.popBackStack() },
                )
            }
        }

        composable("editExpense/{groupName}/{expenseID}"){ navBackStackEntry ->
            val groupName = navBackStackEntry.arguments?.getString("groupName")
            val expenseID = navBackStackEntry.arguments?.getString("expenseID")

            val expenseToEdit = viewModel.groups.find { it.name == groupName }?.expenses?.find { it.id == expenseID }
            AddExpenseScreen(isEdit = Edit(true, expenseToEdit!!),
                viewModel = viewModel,
                groupName = groupName ?: "",
                onExpenseAdded = { navController.popBackStack() }
                )
        }
    }
}