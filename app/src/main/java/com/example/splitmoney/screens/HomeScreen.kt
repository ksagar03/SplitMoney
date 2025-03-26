package com.example.splitmoney.screens

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.splitmoney.R
import com.example.splitmoney.header.Header
import com.example.splitmoney.signuporlogin.AuthViewModel
import com.example.splitmoney.ui.theme.gradient


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    viewModel: SplitMoneyViewModel,
    authViewModel: AuthViewModel,
    onGroupClick: (String) -> Unit,
    onAddGroupClick: () -> Unit,
    onAddExpenseClick: () -> Unit,
    onLogout: () -> Unit,
    onEditGroupClick: (Any?) -> Unit,
    onDeleteGroupClick: (Any?) -> Unit,

    ) {
    val groups = viewModel.groups
//    val listState = rememberLazyListState()


    Scaffold(topBar = {
        Header(
            onLogoutClick = { authViewModel.logout() },
        )
    }, containerColor = colorResource(id = R.color.Dark_Theme)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp)
                .background(gradient),
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),

                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {


                if (groups.isEmpty()) {
                    androidx.compose.animation.AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(animationSpec = tween(durationMillis = 500))
                    ) {
                        Text(
                            "No Groups created yet",
                            style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray),
                            modifier = Modifier.padding((16.dp))
                        )

                    }


                } else {
                    Text(
                        "Groups:",
                        style = MaterialTheme.typography.headlineMedium,
                        color = colorResource(id = R.color.Dark_Theme_Text),
                        modifier = Modifier.padding((16.dp))
                    )
                    LazyColumn(
                        contentPadding = PaddingValues(bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .animateContentSize()
                            .heightIn(max = 500.dp),
                        userScrollEnabled = true,
                    ) {
                        items(groups, key = { it.name }) { group ->

                            androidx.compose.animation.AnimatedVisibility(
                                visible = true,
                                enter = slideInVertically(
                                    animationSpec = tween(durationMillis = 300),
                                    initialOffsetY = { it }) + fadeIn(
                                    animationSpec = tween(
                                        durationMillis = 300
                                    )
                                )
                            ) {
                                GroupItem(
                                    group = group,
                                    onClick = { onGroupClick(group.name) },
                                    modifier = Modifier,
                                    onEditClick = { onEditGroupClick(group.name) },
                                    onDeleteClick = { onDeleteGroupClick(group.name) },
                                    onViewOptions = true,
                                )
                            }
                        }
                    }


                }

                Button(
                    onClick = onAddGroupClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Create New Group")

                }

            }

            FloatingActionButton(
                onClick = onAddExpenseClick,
                modifier = Modifier
                    .padding(30.dp)
                    .width(150.dp)
                    .align(Alignment.BottomEnd),
                containerColor = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(24.dp)


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
fun GroupItem(
    group: Group,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier,
    onViewOptions: Boolean,
) {
    val totalAmount = group.expenses.sumOf { exp -> exp.amount }
    var isPressed by remember { mutableStateOf(false) }
    var isMenuPressed by remember { mutableStateOf(value = false) }
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
    val rotationState by animateFloatAsState(
        targetValue = if (isMenuPressed) 180f else 0f,
        label = ""
    )


    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = true),
                onClick = {
                    isPressed = !isPressed
                    onClick()
                }
            ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = animatedElevation),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.Dark_Theme_Icon))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = group.name,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (onViewOptions) {
                    IconButton(onClick = { isMenuPressed = !isMenuPressed }) {
                        Icon(
                            painter = painterResource(id = R.drawable.more_menu),
                            contentDescription = "menu display",
                            tint = Color.Unspecified,
                            modifier = Modifier.rotate(rotationState)
                        )
                    }
                }

                androidx.compose.animation.AnimatedVisibility(
                    visible = isMenuPressed,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically(),
                ) {
                    Column(modifier = Modifier.width(100.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.clickable { onEditClick() }
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit group")
                            Text("Edit")
                        }
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 4.dp),
                            thickness = 1.dp,
                            color = Color.Gray
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.clickable { onDeleteClick() }
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "delete group")
                            Text("delete")
                        }
                    }
                }

            }

            Text(
                text = "Members : ${group.members.joinToString(", ")}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(bottom = 4.dp)
            )

            AnimatedVisibility(
                visible = animatedAlpha == 1f,
                enter = fadeIn() + slideInHorizontally(),
                exit = fadeOut() + slideOutVertically()
            ) {

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




