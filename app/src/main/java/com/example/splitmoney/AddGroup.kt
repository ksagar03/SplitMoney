package com.example.splitmoney

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun AddGroupScreen(
    viewModel: SplitMoneyViewModel,
    onGroupAdded: () -> Unit

) {
    var groupName by remember { mutableStateOf("") }
    var newMember by remember { mutableStateOf("") }
    val members = remember { mutableStateListOf<String>() }
    var errorMessage by remember { mutableStateOf<String?>(null) }


    Column(modifier = Modifier.padding(16.dp)) {
        errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                modifier = Modifier.padding(bottom = 8.dp)

            )
        }
        OutlinedTextField(
            value = groupName,
            onValueChange = { groupName = it },
            label = { Text("Group Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = newMember,
                onValueChange = { newMember = it },
                label = { Text("Add Member") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            ElevatedButton(onClick = {
                if (newMember.isNotEmpty()) {
                    members.add(newMember)
                    newMember = ""
                }
            }) {
                Text("Add")
            }
        }

        if (members.isNotEmpty()) {
            Text(
                text = "Members:",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            LazyColumn {
                itemsIndexed(members) { _, member ->
                    Text(
                        text = member,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ElevatedButton(onClick = {
            if (groupName.isEmpty() || members.isEmpty()) {
                errorMessage = "Please fill all the fields"

            } else {
                viewModel.addGroup(groupName, members)
                onGroupAdded()
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Save Group")
        }
    }


}