package com.example.splitmoney.signuporlogin.components


import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
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
import androidx.compose.ui.unit.sp
import com.example.splitmoney.R
import kotlinx.coroutines.delay


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
    val focusRequester = remember { FocusRequester() }

//    LaunchedEffect(Unit) {
////        delay(2000L)
//        if (labelText == "Name") {
//            focusRequester.requestFocus()
//        }else if(labelText == "Mail"){
//            focusRequester.requestFocus()
//
//        }
//    }
    var textValue by remember { mutableStateOf("") }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
            .clip(RoundedCornerShape(topStart = 20.dp, bottomEnd = 20.dp))
            .focusRequester(focusRequester),
        label = { Text(text = labelText) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colorResource(id = R.color.Dark_Theme_Icon),
            focusedLabelColor = colorResource(id = R.color.Dark_Theme_Icon),
            cursorColor = colorResource(id = R.color.Dark_Theme_Text),
            focusedContainerColor = colorResource(id = R.color.Dark_Theme_Primary),
            focusedTextColor = colorResource(id = R.color.Dark_Theme_Text),
            focusedLeadingIconColor = colorResource(id = R.color.Dark_Theme_Icon),

            ),
        keyboardOptions = KeyboardOptions.Default,
        value = textValue,
        onValueChange = {
            textValue = it
        },
        leadingIcon = {
            Icon(painter = iconData, contentDescription = iconDescription)
        },
        shape = RoundedCornerShape(topStart = 20.dp, bottomEnd = 20.dp)

    )

}

@Composable
fun UserPasswordTextFiled(labelText: String, iconData: Painter, iconDescription: String) {

    var passwordValue by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
            .clip(RoundedCornerShape(topStart = 20.dp, bottomEnd = 20.dp)),
        label = { Text(text = labelText) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colorResource(id = R.color.Dark_Theme_Icon),
            focusedLabelColor = colorResource(id = R.color.Dark_Theme_Icon),
            cursorColor = colorResource(id = R.color.Dark_Theme_Text),
            focusedContainerColor = colorResource(id = R.color.Dark_Theme_Primary),
            focusedTextColor = colorResource(id = R.color.Dark_Theme_Text),
            focusedLeadingIconColor = colorResource(id = R.color.Dark_Theme_Icon),
            focusedTrailingIconColor = colorResource(id = R.color.Dark_Theme_Icon),
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
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        shape = RoundedCornerShape(topStart = 20.dp, bottomEnd = 20.dp)

    )

}


@Composable
fun ButtonComponent(value: String) {
    Button(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .heightIn(48.dp),
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(
            Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(48.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            colorResource(id = R.color.Dark_Theme_Secondary),
                            colorResource(id = R.color.Dark_Theme_Icon)
                        )
                    ),
                    shape = RoundedCornerShape(45.dp)
                ),
            contentAlignment = Alignment.Center

        ) {
            AnimatedContent(targetState = value, label = "") {
                targetValue ->
                Text(
                    targetValue,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.Dark_Theme_Text)
                )

            }



        }
    }
}

@Composable
fun DividerComponent() {
    Row(modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically) {

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            thickness = 1.dp, color = colorResource(id = R.color.teal_200),
        )
        Text("or", fontSize = 18.sp, color = colorResource(id = R.color.Dark_Theme_Text), modifier = Modifier.padding(8.dp))
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            thickness = 1.dp, color = colorResource(id = R.color.teal_200),
        )

    }

}


//@Composable
//fun AnnotatedText(value: String){
//    val annotatedString = buildAnnotatedString {
//        append("Click")
//        withLink(
//            "login",
//        ){
//            append(value)
//        }
//        append("")
//
//
//    }


//}