package com.example.splitmoney.header

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.splitmoney.R
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(
    onLogoutClick: () -> Unit,
    onProfileClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},

    ) {
    var visible by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    val focusManager = LocalFocusManager.current



    LaunchedEffect(Unit) {
        delay(300)
        visible = true
    }

    BackHandler(enabled = expanded) {
        expanded = false
    }

        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(initialOffsetY = { -100 }) + fadeIn(animationSpec = tween(600)),
            modifier = Modifier.zIndex(1f)
        ) {
            Column {


                TopAppBar(
                    title = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically

                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                SplitMoneyLogoAnimation()

                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                IconButton(
                                    onClick = { expanded = true },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Color.LightGray.copy(alpha = 0.2f))
                                        .padding(horizontal = 4.dp, vertical = 0.5.dp),
                                    content = {
                                        Icon(
                                            painter = painterResource(id = R.drawable.baseline_person_2),
                                            contentDescription = "Profile",
                                            tint = colorResource(id = R.color.Dark_Theme_Icon)
                                        )

                                    }
                                )


                            }


                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.TopEnd)
                        .padding(horizontal = 16.dp)
                ) {
                    androidx.compose.animation.AnimatedVisibility(
                        visible = expanded,
                        enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
                        exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top),
                        modifier = Modifier
                            .wrapContentSize()
                            .offset(x = (-16).dp)
                    ) {
                        Card(
                            modifier = Modifier
                                .width(200.dp)
                                .padding(top = 8.dp)
                                .shadow(8.dp, shape = RoundedCornerShape(12.dp)),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = colorResource(id = R.color.Dark_Theme_Primary),
                                contentColor = colorResource(id = R.color.Dark_Theme_Text),


                                ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            "My Profile",
                                            style = MaterialTheme.typography.bodyLarge,
                                            textDecoration = TextDecoration.LineThrough,
                                            color = colorResource(id = R.color.Dark_Theme_Text)
                                        )
                                    },
                                    onClick = {
                                        onProfileClick()
                                        expanded = false
                                    },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.Person,
                                            contentDescription = "My Profile",
                                            tint = colorResource(id = R.color.Dark_Theme_Icon)
                                        )
                                    }
                                )
                                HorizontalDivider(thickness = 1.dp, color = Color.Gray)

                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            "Settings",
                                            style = MaterialTheme.typography.bodyLarge,
                                            textDecoration = TextDecoration.LineThrough,
                                            color = colorResource(id = R.color.Dark_Theme_Text)
                                        )
                                    },
                                    onClick = {
                                        expanded = false
                                    },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.Settings,
                                            contentDescription = "Settings",
                                            tint = colorResource(id = R.color.Dark_Theme_Icon)

                                        )
                                    }
                                )
                                HorizontalDivider(thickness = 1.dp, color = Color.Gray)
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            "Log Out",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    },
                                    onClick = {
                                        onLogoutClick()
                                        expanded = false
                                    },
                                    leadingIcon = {
                                        Icon(
                                            painter = painterResource(R.drawable.baseline_logout_24),
                                            contentDescription = "Log Out",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }

    }
    if (expanded) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(interactionSource = interactionSource, indication = null) {
                    expanded = false
                    focusManager.clearFocus()
                })
    }

}

//@Preview
//@Composable
//fun Uidisp(){
//    Header(onLogoutClick = {})
//}