package com.example.splitmoney.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.splitmoney.R


data class textFieldParameters(
    val label: String,
    val value: String,
    val onValueChange: (String) -> Unit,

    )

@Composable
fun MyOutlinedTextField(textFieldParameters: textFieldParameters, modifier: Modifier) {
    OutlinedTextField(
        value = textFieldParameters.value,
        onValueChange = textFieldParameters.onValueChange,
        label = { Text(textFieldParameters.label) },
        shape = RoundedCornerShape(12.dp),
        modifier = modifier,
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


}

@Composable
fun MyDropDownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onMenuItemClick: (String) -> Unit,
    dropDownOffset: () -> Offset,
) {
    val density = LocalDensity.current

    DropdownMenu(
        expanded = expanded, onDismissRequest = onDismissRequest, offset = with(density) {
            DpOffset(
                x = dropDownOffset().x.toDp() - 48.dp,
                y = dropDownOffset().y.toDp() - 20.dp
            )
        },
        modifier = Modifier.background(
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            shape = MaterialTheme.shapes.extraLarge
        )
    ) {
        DropdownMenuItem(
            {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit group")
                    Text("Edit")
                }
            },
            onClick = { onMenuItemClick("Edit") }

        )
        DropdownMenuItem(
            {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "delete group")
                    Text("delete")
                }
            },
            onClick = { onMenuItemClick("Delete") }
        )


    }

}