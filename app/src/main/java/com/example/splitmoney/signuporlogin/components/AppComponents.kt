package com.example.splitmoney.signuporlogin.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.splitmoney.R


@Composable
fun TextComponent(value: String, fontSize: TextUnit, fontWeight: FontWeight) {
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp)
            .background(colorResource(id = R.color.Dark_Theme)),
        style = TextStyle(
            fontSize = fontSize,
            fontWeight = fontWeight,
            fontStyle = FontStyle.Normal,
            color = colorResource(id = R.color.Dark_Theme_Text)
        ),
        textAlign = TextAlign.Center

    )
}

@Composable
fun UserTextFields(labelText: String, iconData: Painter, iconDescription: String) {
    var textValue by remember { mutableStateOf("") }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)),
        label = { Text(text = labelText) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colorResource(id = R.color.Dark_Theme_Secondary),
            focusedLabelColor = colorResource(id = R.color.teal_700),
            cursorColor = colorResource(id = R.color.Dark_Theme_Text),
            focusedContainerColor = colorResource(id = R.color.Dark_Theme_Primary),
        ),
        keyboardOptions = KeyboardOptions.Default,
        value = textValue,
        onValueChange = {
            textValue = it
        },
        leadingIcon = {
            Icon(painter = iconData, contentDescription = iconDescription)
        }

    )

}

@Composable
fun UserPasswordTextFiled(labelText: String, iconData: Painter, iconDescription: String) {
    var passwordValue by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)),
        label = { Text(text = labelText) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colorResource(id = R.color.Dark_Theme_Secondary),
            focusedLabelColor = colorResource(id = R.color.teal_700),
            cursorColor = colorResource(id = R.color.Dark_Theme_Text),
            focusedContainerColor = colorResource(id = R.color.Dark_Theme_Primary),
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),

        value = passwordValue,
        onValueChange = {
            passwordValue = it
        },
        leadingIcon = {
            Icon(painter = iconData, contentDescription = iconDescription)
        },
        trailingIcon = {
            val iconImage = if (passwordVisible) {
                ImageVector.vectorResource(id = R.drawable.visibility_icon)
            } else {
                ImageVector.vectorResource(id = R.drawable.visibility_off_icon)
            }
            val description = if (passwordVisible) {
                "hide password"
            } else {
                "show password"
            }


            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = iconImage, contentDescription = description)
            }


        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()

    )

}