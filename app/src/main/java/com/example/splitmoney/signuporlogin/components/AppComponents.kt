package com.example.splitmoney.signuporlogin.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
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