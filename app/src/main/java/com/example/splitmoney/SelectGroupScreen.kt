package com.example.splitmoney

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun SelectGroupScreen(
    groups: List<Group>,
    onGroupSelected: (String) -> Unit,
    onCreateNewGroup: () -> Unit

) {
//    val groups = viewModel.groups
    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (groups.isEmpty()) {
                Text(
                    "No groups available. Create a new group to add expenses",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 16.dp),
                    textAlign = TextAlign.Center
                )
                Button(
                    onClick = {onCreateNewGroup()},
                ) {
                    Text("Create New Group")
                }

            } else {

                Text(
                    "Select a group to add expense:",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                LazyColumn {
                    itemsIndexed(groups) { _, group ->
                        GroupItem(
                            group = group,
                            onClick = { onGroupSelected(group.name) }
                        )
                    }
                }

            }


        }


    }
}


//@Preview
//@Composable
//fun ToView(){
//    SelectGroupScreen()
//}