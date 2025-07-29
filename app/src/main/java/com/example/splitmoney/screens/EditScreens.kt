package com.example.splitmoney.screens

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.splitmoney.R
import com.example.splitmoney.models.Group

@SuppressLint("UnrememberedMutableState")
@Composable
fun EditGroupScreen(group: Group, onSave: (String, List<String>) -> Unit, onCancel: () -> Unit) {
    var groupName by remember { mutableStateOf(group.name) }
    var members by remember { mutableStateOf(group.members.joinToString(", ")) }

    val buttonEnabled by derivedStateOf {
        groupName.isNotBlank() && members.isNotBlank()
    }

    val animatedProgress by animateFloatAsState(
        targetValue = if (buttonEnabled) 1f else 0.6f,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        androidx.compose.animation.AnimatedVisibility(
            visible = true,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            OutlinedTextField(
                value = groupName,
                onValueChange = { groupName = it },
                label = { Text("Group Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorResource(id = R.color.Dark_Theme_Icon),
                    focusedLabelColor = colorResource(id = R.color.Dark_Theme_Icon),
                    cursorColor = colorResource(id = R.color.Dark_Theme_Text),
                    focusedContainerColor = colorResource(id = R.color.Dark_Theme_Primary),
                    focusedTextColor = colorResource(id = R.color.Dark_Theme_Text),
                    focusedLeadingIconColor = colorResource(id = R.color.Dark_Theme_Icon),
                    unfocusedTextColor = colorResource(id = R.color.Dark_Theme_Text),
                    errorBorderColor = Color.Red,
                    errorLabelColor = Color.Red
                )
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        AnimatedVisibility(
            visible = true,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            OutlinedTextField(
                value = members,
                onValueChange = { members = it },
                label = { Text("Members (comma-separated)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorResource(id = R.color.Dark_Theme_Icon),
                    focusedLabelColor = colorResource(id = R.color.Dark_Theme_Icon),
                    cursorColor = colorResource(id = R.color.Dark_Theme_Text),
                    focusedContainerColor = colorResource(id = R.color.Dark_Theme_Primary),
                    focusedTextColor = colorResource(id = R.color.Dark_Theme_Text),
                    focusedLeadingIconColor = colorResource(id = R.color.Dark_Theme_Icon),
                    unfocusedTextColor = colorResource(id = R.color.Dark_Theme_Text)
                )
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onCancel,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.Dark_Theme_alert)
                )
            ) {
                Text("Cancel")


            }
            Button(
                onClick = {
                    onSave(groupName, members.split(",").map { it.trim() })
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                enabled = buttonEnabled,
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.Dark_Theme_Secondary2))
            ) {
                Text("Save")
            }
        }

    }


}


@Composable
fun EditExpenseScreen() {

}