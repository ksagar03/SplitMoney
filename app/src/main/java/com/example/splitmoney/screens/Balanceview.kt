package com.example.splitmoney.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.xr.compose.testing.toDp
import com.example.splitmoney.R
import com.example.splitmoney.models.Expense
import kotlinx.coroutines.delay
import kotlin.math.round


@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun BalanceSummaryScreen(
    viewModel: SplitMoneyViewModel,
    groupID: String,
    onBlockClick: () -> Unit,
    navController: NavController,
) {
    val balances = viewModel.calculateBalances(groupID)

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            "Balance Summary for $groupID",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .animateContentSize(),
            color = colorResource(id = R.color.Dark_Theme_Text)

        )
        if (balances.isEmpty()) {
            Text(
                "no Expenses added yet",
                style = MaterialTheme.typography.bodyMedium,
                color = colorResource(id = R.color.Dark_Theme_Text)
            )

        } else {
            LazyColumn {
                items(balances.entries.toList()) { (member, balance) ->
                    val isPositive = balance >= 0
                    val color =
                        if (isPositive) colorResource(id = R.color.teal_700) else colorResource(id = R.color.Dark_Theme_alert)
                    val icon =
                        if (isPositive) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown

                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + slideInVertically(),
                        exit = fadeOut() + slideOutVertically()
                    ) {
                        Card(
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = member,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = colorResource(id = R.color.Dark_Theme),
                                    modifier = Modifier.weight(1f)
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = if (isPositive) "Gets" else "Owes",
                                        tint = color,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "₹${round(balance)}",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = color
                                    )
                                }

                            }

                        }

                    }

                }

            }
            ExpenseView(groupID = groupID, viewModel = viewModel, navController = navController)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onBlockClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .animateContentSize()
        ) {
            Text("Back")
        }

    }
}


@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun ExpenseView(
    viewModel: SplitMoneyViewModel,
    groupID: String,
    navController: NavController,
) {
    val groupExpenses = viewModel.viewExpensesOfGroup(groupID)
    var isClicked by remember { mutableStateOf(false) }
    var isPulsing by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPulsing) 1.2f else 1f,
        animationSpec = tween(durationMillis = 500, easing = EaseInOut), label = ""
    )
    val rotation by animateFloatAsState(
        targetValue = if (isClicked) 90f else 0f,
        animationSpec = tween(durationMillis = 300), label = ""
    )

    LaunchedEffect(isClicked) {
        while (!isClicked) {
            isPulsing = !isPulsing
            delay(800)
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isClicked = !isClicked }
                .animateContentSize(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.Dark_Theme_Icon))
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "View \"$groupID\" Expenses",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "View Expenses",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(24.dp)
                        .scale(scale)
                        .rotate(rotation)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        AnimatedVisibility(
            visible = isClicked,
            enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
            exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(groupExpenses) { (id, expense, amount, payer) ->
                    var showActions by remember { mutableStateOf(false) }
                    var cardHeight by remember { mutableStateOf(0.dp) }
                    val haptic = LocalHapticFeedback.current
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .onSizeChanged { size -> cardHeight = size.height.toDp() }
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .pointerInput(Unit) {
                                    detectTapGestures(onLongPress = {
                                        haptic.performHapticFeedback(
                                            HapticFeedbackType.LongPress
                                        )
                                        showActions = true
                                    },
                                        onPress = {})
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = if (showActions) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = expense,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    Text(
                                        text = "Paid by: $payer",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Text(
                                    text = "₹$amount",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                        }

                        if (showActions) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(cardHeight)
                                    .background(
                                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f),
                                        shape = MaterialTheme.shapes.medium
                                    )
                                    .clickable { showActions = false }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    IconButton(
                                        onClick = {
                                            navController.navigate("editExpense/${groupID}/${id}")
                                            showActions = false
                                        },


                                        modifier = Modifier
                                            .size(48.dp)
                                            .background(
                                                color = MaterialTheme.colorScheme.primaryContainer,
                                                shape = CircleShape
                                            )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit",
                                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                                        )

                                    }

                                    IconButton(
                                        onClick = {
                                            viewModel.deleteExpense(
                                                expense = Expense(
                                                    id, expense, amount, payer, groupID
                                                )
                                            )
                                            showActions = false
                                        },
                                        modifier = Modifier
                                            .size(48.dp)
                                            .background(
                                                color = MaterialTheme.colorScheme.errorContainer,
                                                shape = CircleShape
                                            )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = MaterialTheme.colorScheme.onErrorContainer
                                        )

                                    }


                                }
                            }
                        }
                    }

                }
            }
        }
    }
}