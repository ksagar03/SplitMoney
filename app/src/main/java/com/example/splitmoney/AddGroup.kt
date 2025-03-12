package com.example.splitmoney

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun AddGroupScreen(
    viewModel: SplitMoneyViewModel,
    onGroupAdded: () -> Unit,

    ) {
    var groupName by remember { mutableStateOf("") }
    var newMember by remember { mutableStateOf("") }
    val members = remember { mutableStateListOf<String>() }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isMemberInputVisible by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)

    ) {
        Text(
            text = "Create New Group",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 50.dp),
            color = colorResource(id = R.color.Dark_Theme_Text)
        )




        AnimatedVisibility(visible = errorMessage != null, enter = fadeIn(), exit = fadeOut()) {
            errorMessage?.let {
                Text(
                    text = it,
                    color = colorResource(id = R.color.Dark_Theme_alert),
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }


        }

        OutlinedTextField(
            value = groupName,
            onValueChange = { groupName = it },
            label = { Text("Group Name") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
//                focusedBorderColor = colorResource(id = R.color.Dark_Theme_Secondary),
                unfocusedBorderColor = colorResource(id = R.color.Dark_Theme_Secondary2),
                unfocusedLabelColor = colorResource(id = R.color.teal_700),
                focusedBorderColor = colorResource(id = R.color.Dark_Theme_Icon),
                focusedLabelColor = colorResource(id = R.color.Dark_Theme_Icon),
                cursorColor = colorResource(id = R.color.Dark_Theme_Text),
                focusedContainerColor = colorResource(id = R.color.Dark_Theme_Primary),
                focusedTextColor = colorResource(id = R.color.Dark_Theme_Text),
                focusedLeadingIconColor = colorResource(id = R.color.Dark_Theme_Icon),
                focusedTrailingIconColor = colorResource(id = R.color.Dark_Theme_Icon),
                unfocusedTextColor = colorResource(id = R.color.Dark_Theme_Text),
            )
        )

        ElevatedButton(
            onClick = { isMemberInputVisible = !isMemberInputVisible },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(if (isMemberInputVisible) " Hid Member Input" else "Add Members")
        }
        AnimatedVisibility(
            visible = isMemberInputVisible,
            enter = slideInVertically(initialOffsetY = { -40 }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -40 }) + fadeOut()
        ) {


            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),

                ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newMember,
                        onValueChange = { newMember = it },
                        label = { Text("Add Member") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(

                            unfocusedBorderColor = colorResource(id = R.color.Dark_Theme_Secondary2),
                            unfocusedLabelColor = colorResource(id = R.color.teal_700),
                            focusedBorderColor = colorResource(id = R.color.Dark_Theme_Icon),
                            focusedLabelColor = colorResource(id = R.color.Dark_Theme_Icon),
                            cursorColor = colorResource(id = R.color.Dark_Theme_Text),
                            focusedContainerColor = colorResource(id = R.color.Dark_Theme_Primary),
                            focusedTextColor = colorResource(id = R.color.Dark_Theme_Text),
                            focusedLeadingIconColor = colorResource(id = R.color.Dark_Theme_Icon),
                            focusedTrailingIconColor = colorResource(id = R.color.Dark_Theme_Icon),
                            unfocusedTextColor = colorResource(id = R.color.Dark_Theme_Text),
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            if (newMember.isNotEmpty()) {
                                members.add(newMember)
                                newMember = ""
                            }
                        },
                    ) {
                        Icon(
                            painterResource(id = R.drawable.addicon),
                            contentDescription = "Add Member",
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .size(45.dp)
                        )

                    }

                }

            }
        }

        AnimatedVisibility(
            visible = members.isNotEmpty(),
            enter = fadeIn(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            ),
            exit = fadeOut(animationSpec = tween(durationMillis = 200))
        ) {
            Column {
                Text(
                    text = "Members:",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = colorResource(id = R.color.Dark_Theme_Text)
                )
                LazyColumn(modifier = Modifier.heightIn(max = 200.dp)) {
                    itemsIndexed(members) { _, member ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painterResource(id = R.drawable.account_icon),
                                contentDescription = "Members",
                                tint = Color.Unspecified
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = member, fontSize = 16.sp, color = colorResource(id = R.color.Dark_Theme_Text))
                        }
                    }
                }
            }
        }


        ElevatedButton(onClick = {
            if (groupName.isEmpty() || members.isEmpty()) {
                errorMessage = "Please fill all the fields"

            } else {
                viewModel.addGroup(groupName, members)
                onGroupAdded()
            }
        }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
            Text("Create Group")
        }
    }


}


@Preview
@Composable
fun ToView() {
    val viewModel = SplitMoneyViewModel()
    AddGroupScreen(viewModel, onGroupAdded = {})
}