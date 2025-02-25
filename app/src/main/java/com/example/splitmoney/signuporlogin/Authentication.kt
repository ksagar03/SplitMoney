package com.example.splitmoney.signuporlogin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.splitmoney.R
import com.example.splitmoney.signuporlogin.components.TextComponent
import com.example.splitmoney.signuporlogin.components.UserPasswordTextFiled
import com.example.splitmoney.signuporlogin.components.UserTextFields

@Composable
fun SignUpScreen() {
    Surface(
        color = colorResource(id = R.color.Dark_Theme),
        modifier = Modifier
            .fillMaxSize()

    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(28.dp)) {
            TextComponent(
                value = stringResource(id = R.string.SignupHeading),
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal
            )
            TextComponent(
                value = stringResource(id = R.string.SignupHeading2),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(100.dp))
            UserTextFields(
                labelText = stringResource(id = R.string.userName),
                iconData = painterResource(id = R.drawable.account_icon),
                iconDescription = "userNameIcon"
            )
            UserTextFields(
                labelText = stringResource(id = R.string.userEmail),
                iconData = painterResource(id = R.drawable.outline_email_icon),
                iconDescription = "userEmailIcon"
            )
            UserPasswordTextFiled(
                labelText = stringResource(id = R.string.userPassword),
                iconData = painterResource(id = R.drawable.baseline_lock_icon),
                iconDescription = "userNameIcon"
            )
        }


    }
}


@Preview
@Composable
fun DefaultPreviewOfSignUpScreen() {
    SignUpScreen()
}