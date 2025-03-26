package com.example.splitmoney.screens

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
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.splitmoney.R
import com.example.splitmoney.screens.components.MyDropDownMenu
import kotlinx.coroutines.delay
import kotlin.math.round

@Composable
fun BalanceSummaryScreen(
    viewModel: SplitMoneyViewModel,
    groupName: String,
    onBlockClick: () -> Unit,
) {
    val balances = viewModel.calculateBalances(groupName)

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            "Balance Summary for $groupName",
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
            ExpenseView(groupName = groupName, viewModel = viewModel)
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


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpenseView(
    viewModel: SplitMoneyViewModel,
    groupName: String,
) {
    val groupExpenses = viewModel.ViewExpensesOfGroup(groupName)
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
                    text = "View \"$groupName\" Expenses",
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
                items(groupExpenses) { (expense, amount, payer) ->
                    var showMenu by remember { mutableStateOf(false) }
                    var dropDownOffset by remember { mutableStateOf(Offset.Zero) }
                    var isBeingPressed by remember { mutableStateOf(false) }
                    val haptic = LocalHapticFeedback.current
                    fun EditExpense() {}
                    fun DeleteExpense() {}
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                    Card(
                        modifier = Modifier.pointerInput(Unit) {
                            detectTapGestures(
                                onPress = { offset ->
                                    isBeingPressed = true
                                    dropDownOffset = offset
                                    tryAwaitRelease()
                                    isBeingPressed = false
                                },
                                onLongPress = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    showMenu = true
                                }

                            )
                        },
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isBeingPressed) MaterialTheme.colorScheme.surfaceVariant.copy(
                                alpha = 0.6f
                            ) else MaterialTheme.colorScheme.surface
                        ),

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
                    MyDropDownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        onMenuItemClick = { it -> if (it == "Edit") EditExpense() else DeleteExpense() },
                        dropDownOffset = { dropDownOffset})
                }
            }
        }
    }
}