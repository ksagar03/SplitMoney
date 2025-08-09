package com.example.splitmoney.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.splitmoney.models.Expense
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
                onGroupClick = { groupID -> navController.navigate("balanceView/${groupID}") },
                onAddExpenseClick = {
                    navController.navigate(("addExpense"))
                },
                onAddGroupClick = { navController.navigate("addGroup") },
                onLogout = {

                        try{
                            authViewModel.logout()
                            navController.navigate("auth") {
                                popUpTo("auth") {
                                    inclusive = true
                                }
                            }
                        }catch (e: Exception){
                            Log.d("AuthState_Navigation_Issue", "Navigation: ")
                    }

                },
                onEditGroupClick = { groupID -> navController.navigate("editGroup/${groupID}") },
                onDeleteGroupClick = { groupID -> viewModel.deleteGroup(groupID.toString()) }
            )
        }

        composable("addExpense/{groupID}") { backStackEntry ->
            val groupID = backStackEntry.arguments?.getString("groupID")

            AddExpenseScreen(
                viewModel = viewModel,
                groupID = groupID ?: "",
                onExpenseAdded = { navController.popBackStack() },
                isEdit = Edit(false, Expense("","", 0.0, "", ""))
                )


        }

        composable("balanceView/{groupID}") { navBackStackEntry ->
            val groupID = navBackStackEntry.arguments?.getString("groupID")
            if (groupID != null) {
                BalanceSummaryScreen(
                    viewModel = viewModel,
                    groupID = groupID,
                    onBlockClick = {
                        navController.popBackStack()
                    },
                    navController = navController
                )
            }
        }

        composable("addGroup") {
            AddGroupScreen(viewModel = viewModel, onGroupAdded = { navController.popBackStack() })
        }

        composable("addExpense") {
            SelectGroupScreen(
                onGroupSelected = { groupID ->
                    navController.navigate("addExpense/$groupID")
                },
                onCreateNewGroup = {
                    navController.navigate("addGroup")

                }
            )
        }


        composable("editGroup/{groupID}") { navBackStackEntry ->
            val groupID = navBackStackEntry.arguments?.getString("groupID")
            val group = viewModel.groups.collectAsState().value.find { it.id == groupID }
            if (group != null) {
                EditGroupScreen(
                    group = group,
                    onSave = { newName, newMembers ->
                        if (groupID != null) {
                            viewModel.editGroup(groupID, newName, newMembers)
                        }
                        navController.popBackStack()
                    },
                    onCancel = { navController.popBackStack() },
                )
            }
        }

        composable("editExpense/{groupID}/{expenseID}"){ navBackStackEntry ->
            val groupID = navBackStackEntry.arguments?.getString("groupID")
            val expenseID = navBackStackEntry.arguments?.getString("expenseID")

            val expenseToEdit = viewModel.groups.collectAsState().value.find { it.id == groupID }?.expenses?.find { it.id == expenseID }
            if (groupID != null) {
                AddExpenseScreen(isEdit = Edit(true, expenseToEdit!!),
                    viewModel = viewModel,
                    groupID = groupID,
                    onExpenseAdded = { navController.popBackStack() }
                )
            }
        }
    }
}

