package com.example.splitmoney.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
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
import com.example.splitmoney.models.Group
import kotlinx.coroutines.delay
import java.util.Locale.getDefault


@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun BalanceSummaryScreenV2(
    viewModel: SplitMoneyViewModel,
    groupID: String,
    onBlockClick: () -> Unit,
    navController: NavController,
) {
        LaunchedEffect(groupID) {
        viewModel.setCurrentGroup(groupID)
    }
    val groupDetails = viewModel.getGroupInfo(groupID)
    val calBalanceV2 = viewModel.calculateBalancesV2(groupID)
    val expenses by viewModel.currentGroupExpenses.collectAsState()
    val totalAmount = expenses.sumOf { it.amount }
//    Log.d("newBalanceView", "BalanceSummaryScreen: $calBalanceV2")

//    val animatedVisibility = remember { Animatable(0f) }
//    LaunchedEffect(Unit) {
//        animatedVisibility.animateTo(1f, animationSpec = tween(500))
//
//    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
//            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = { onBlockClick() },
                modifier = Modifier.size(48.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Balance Summary for",
                style = MaterialTheme.typography.headlineMedium,
                color = colorResource(R.color.Dark_Theme_Text),
                modifier = Modifier.weight(1f)


            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = groupDetails?.name ?: "Group",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "${groupDetails?.members?.size ?: 0} members",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                        )
                        Text(
                            text = "Amount Spent: ₹$totalAmount",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                        )
                    }
                }

            }

        }
        Spacer(modifier = Modifier.height(23.dp))

        if (calBalanceV2.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "No Expenses added yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorResource(R.color.Dark_Theme_Text)
                )
            }
        } else {
            Text(
                "Settlements needed",
                style = MaterialTheme.typography.titleMedium,
                color = colorResource(R.color.Dark_Theme_Text),
                modifier = Modifier.padding(8.dp)
            )
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)

            ) {
                var index = 0
                calBalanceV2.forEach { (debtor, balanceMap) ->
                    balanceMap.forEach { (creditor, amount) ->
                        if (amount > 0) {
                            item("$debtor-$creditor-${amount.hashCode()}") {

                                SettlementCard(
                                    debtor = debtor,
                                    creditor = creditor,
                                    amount = amount,
                                    index = index++,
                                )


                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))

            ExpenseView(
                groupDetails = groupDetails,
                expenses = expenses,
                navController = navController,
                viewModel = viewModel
            )
        }

    }

}


@SuppressLint("RestrictedApi")
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun ExpenseView(
    viewModel: SplitMoneyViewModel,
    groupDetails: Group?,
    expenses: List<Expense>,
    navController: NavController,
) {


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
                    text = "View \"${groupDetails?.name}\" Expenses",
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
                items(expenses) { (id, expense, amount, payer) ->
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
                                    detectTapGestures(
                                        onLongPress = {
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
                                            navController.navigate("editExpense/${groupDetails?.id}/${id}")
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
                                                    id, expense, amount, payer, groupDetails?.id
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




@Composable
private fun SettlementCard(
    debtor: String,
    creditor: String,
    amount: Double,
    modifier: Modifier = Modifier,
    index: Int,
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(index * 80L)
        visible = true
    }

    val offsetY by animateDpAsState(
        targetValue = if (visible) 0.dp else 30.dp, // slide from 30dp down
        animationSpec = spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = Spring.DampingRatioMediumBouncy
        ),
        label = "offsetY"
    )

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(400, easing = FastOutSlowInEasing),
        label = "alpha"
    )


//    AnimatedVisibility(
//        visible = visible,
//        enter = slideInVertically(
//            initialOffsetY = { it / 3 },
//            animationSpec = spring(
//                stiffness = Spring.StiffnessLow,
//                dampingRatio = Spring.DampingRatioMediumBouncy,
//            )
//        ) + fadeIn(
//            animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
//        ),
//        exit = slideOutVertically() + fadeOut(),
//    ) {
//    The Animation is not smoother in this little bit glitter is there
        Card(
            modifier = modifier.fillMaxWidth().offset(y =offsetY).alpha(alpha),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)

        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),

                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MemberAvatar(name = debtor, isDebtor = true)
                    Text(debtor.replaceFirstChar { if (it.isLowerCase()) it.titlecase(getDefault()) else it.toString() })
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "owes",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "₹${"%.2f".format(amount)}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MemberAvatar(name = creditor, isDebtor = false)
                    Text(creditor.replaceFirstChar { if (it.isLowerCase()) it.titlecase(getDefault()) else it.toString() })

                }

            }
        }
//    }



}

@Composable
private fun MemberAvatar(name: String, isDebtor: Boolean) {
    val color = if (isDebtor) Color(0xFFFF6B6B) else Color(0xFF4ECDC4)
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(
                color = color.copy(alpha = 0.2f),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name.take(1).uppercase(),
            style = MaterialTheme.typography.titleMedium,
            color = color
        )
    }
}