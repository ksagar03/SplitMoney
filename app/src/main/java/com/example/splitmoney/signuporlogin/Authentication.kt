package com.example.splitmoney.signuporlogin

import androidx.compose.animation.AnimatedVisibility

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.splitmoney.R
import com.example.splitmoney.signuporlogin.components.ButtonComponent
import com.example.splitmoney.signuporlogin.components.DividerComponent
import com.example.splitmoney.signuporlogin.components.TextComponent
import com.example.splitmoney.signuporlogin.components.UserPasswordTextFiled
import com.example.splitmoney.signuporlogin.components.UserTextFields


class AuthState {
    var isSignUpScreen by mutableStateOf(true)

    fun toggleAuthMode() {
        isSignUpScreen = !isSignUpScreen
    }
}


@Composable
fun rememberAuthState(): AuthState {
    return remember { AuthState() }
}


@Composable
fun AuthScreen() {
    val authState = rememberAuthState()

    Surface(
        color = colorResource(id = R.color.Dark_Theme),
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp)
        ) {
            TextComponent(
                value = if (authState.isSignUpScreen) stringResource(id = R.string.SignupHeading) else "Welcome Back",
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal
            )
            TextComponent(
                value = if (authState.isSignUpScreen) stringResource(id = R.string.SignupHeading2) else "Login",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(100.dp))
            AnimatedVisibility(visible = authState.isSignUpScreen) {
                UserTextFields(
                    labelText = stringResource(id = R.string.userName),
                    iconData = painterResource(id = R.drawable.account_icon),
                    iconDescription = "userNameIcon"
                )
            }

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

            Spacer(modifier = Modifier.height(80.dp))
            ButtonComponent(value = if (authState.isSignUpScreen) "Sign Up" else "Login")
            DividerComponent()

            Row {
                Text(
                    modifier = Modifier.padding(vertical = 10.dp),
                    text = if (authState.isSignUpScreen) "Already have an account?" else "Don't have an account?",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                TextButton(onClick = { authState.toggleAuthMode() }) {
                    Text(if (authState.isSignUpScreen) "Login" else "Sign Up")
                }
            }

        }
    }
}

//@Preview
//@Composable
//fun DefaultPreviewOfSignUpScreen() {
//    SignUpScreen()
//}