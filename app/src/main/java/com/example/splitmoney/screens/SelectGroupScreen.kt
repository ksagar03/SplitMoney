package com.example.splitmoney.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.splitmoney.R


@Composable
fun SelectGroupScreen(
    groups: List<Group>,
    onGroupSelected: (String) -> Unit,
    onCreateNewGroup: () -> Unit,

    ) {
//    val groups = viewModel.groups
    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = groups.isEmpty(),
            enter = fadeIn(animationSpec = tween(durationMillis = 300)),
            exit = fadeOut(animationSpec = tween(durationMillis = 300))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "No groups available. Create a new group to add expenses",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 16.dp),
                    textAlign = TextAlign.Center,
                    color = colorResource(id = R.color.Dark_Theme_Text)
                )
                Button(
                    onClick = { onCreateNewGroup() },
                    modifier = Modifier.scale(
                        animateFloatAsState(
                            targetValue = 1f, animationSpec = tween(durationMillis = 500),
                            label = ""
                        ).value
                    )
                ) {
                    Text("Create New Group")
                }
            }

        }

        AnimatedVisibility(
            visible = groups.isNotEmpty(),
            enter = fadeIn(animationSpec = tween(durationMillis = 300)),
            exit = fadeOut(animationSpec = tween(durationMillis = 200))
        ) {

            Column(
                modifier = Modifier
                    .padding(top = 80.dp, start = 16.dp, end = 16.dp)
            ) {
                Text(
                    "Select a group to add expense:",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = colorResource(id = R.color.Dark_Theme_Text)
                )
                LazyColumn {
                    itemsIndexed(groups) { index, group ->

                        val animatedAlpha = animateFloatAsState(
                            targetValue = 1f,
                            animationSpec = tween(durationMillis = 300 + index * 100),
                            label = ""
                        )
                        GroupItem(
                            group = group,
                            onClick = { onGroupSelected(group.name) },
                            modifier = Modifier.alpha(animatedAlpha.value),
                            onEditClick = {},
                            onDeleteClick = {}
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