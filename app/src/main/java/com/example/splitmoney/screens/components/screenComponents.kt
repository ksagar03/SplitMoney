package com.example.splitmoney.screens.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.splitmoney.R


data class textFieldParameters(
    val label: String,
    val value: String,
    val onValueChange: (String) -> Unit,

)
@Composable
fun MyOutlinedTextField(textFieldParameters: textFieldParameters, modifier: Modifier){
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