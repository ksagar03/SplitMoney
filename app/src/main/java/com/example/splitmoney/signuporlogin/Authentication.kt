package com.example.splitmoney.signuporlogin

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.splitmoney.R
import com.example.splitmoney.signuporlogin.components.ButtonComponent
import com.example.splitmoney.signuporlogin.components.DividerComponent
import com.example.splitmoney.signuporlogin.components.TextComponent
import com.example.splitmoney.signuporlogin.components.UserPasswordTextFiled
import com.example.splitmoney.signuporlogin.components.UserTextFields


class AuthState {
    var isSignUpScreen by mutableStateOf(false)

    fun toggleAuthMode() {
        isSignUpScreen = !isSignUpScreen
    }
}


@Composable
fun rememberAuthState(): AuthState {
    return remember { AuthState() }
}


@Composable
fun AuthScreen(viewModel: AuthViewModel, onSuccess: () -> Unit) {
    val authState = rememberAuthState()
    var userName by remember { mutableStateOf("") }
    var emailValue by remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }


    val infiniteTransition = rememberInfiniteTransition(label = "")
    val gradientOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )


    Surface(
        color = colorResource(id = R.color.Dark_Theme),
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
//                .background(
//                    brush = Brush.linearGradient(
//                        colors = listOf(
//                            colorResource(id = R.color.Dark_Theme_Secondary),
//                            colorResource(id = R.color.Dark_Theme_alert),
//
//                            ),
//                        start = Offset(0f, gradientOffset),
//                        end = Offset(1f, 1f - gradientOffset)
//                    )
//                )
                .imePadding()
        ) {


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(28.dp),
//                verticalArrangement = Arrangement.Bottom,
//                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(1f))

                AnimatedContent(
                    targetState = authState.isSignUpScreen,
                    transitionSpec = {
                        slideInVertically { height -> height } + fadeIn() togetherWith slideOutVertically { height -> height } + fadeOut()

                    }, label = ""
                ) { isSignUp ->
                    Column {
                        TextComponent(
                            value = if (isSignUp) stringResource(id = R.string.SignupHeading) else "Welcome Back",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Normal
                        )
                        TextComponent(
                            value = if (isSignUp) stringResource(id = R.string.SignupHeading2) else "Login",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )

                    }

                }



                Spacer(modifier = Modifier.height(50.dp))
                AnimatedVisibility(
                    visible = authState.isSignUpScreen,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    UserTextFields(
                        labelText = stringResource(id = R.string.userName),
                        iconData = painterResource(id = R.drawable.account_icon),
                        iconDescription = "userNameIcon",
                        textValue = userName,
                        onValueChange = { userName = it }

                    )
                }

                UserTextFields(
                    labelText = stringResource(id = R.string.userEmail),
                    iconData = painterResource(id = R.drawable.outline_email_icon),
                    iconDescription = "userEmailIcon",
                    textValue = emailValue,
                    onValueChange = { emailValue = it }

                )
                UserPasswordTextFiled(
                    labelText = stringResource(id = R.string.userPassword),
                    iconData = painterResource(id = R.drawable.baseline_lock_icon),
                    iconDescription = "userNameIcon",
                    passwordValue = passwordValue,
                    onValueChange = { passwordValue = it }
                )
                Spacer(modifier = Modifier.height(20.dp))
                errorMessage?.let {
                    Text(text = it, color = colorResource(id = R.color.Dark_Theme_alert))
                }
                Spacer(modifier = Modifier.height(40.dp))

                AnimatedContent(
                    targetState = authState.isSignUpScreen,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = ""
                ) { isSignUp ->
                    ButtonComponent(
                        value = if (isSignUp) "Sign Up" else "Login",
                        onButtonClicked = {
                            if (isSignUp) {
                                if (userName.isEmpty() || emailValue.isEmpty() || passwordValue.isEmpty()) {
                                    errorMessage = "Please fill all the fields"
                                    return@ButtonComponent
                                }
                                viewModel.signUp(
                                    userName,
                                    emailValue,
                                    passwordValue,
                                    onResult = { success, message ->
                                        if (success) {
                                            onSuccess()
                                        } else {
                                            errorMessage = message ?: "Unknown error"
                                        }
                                    })
                            } else {
                                if (emailValue.isEmpty() || passwordValue.isEmpty()) {
                                    errorMessage = "Please fill all the fields"
                                    return@ButtonComponent
                                }
                                viewModel.login(
                                    emailValue,
                                    passwordValue,
                                    onResult = { success, message ->
                                        if (success) {
                                            onSuccess()
                                        } else {
                                            errorMessage = message ?: "Unknown error"
                                        }
                                    })
                            }
                        })
                }

                DividerComponent()

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy((-7).dp)
                ) {
                    Text(
                        text = if (authState.isSignUpScreen) "Already have an account?" else "Don't have an account?",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colorResource(id = R.color.Dark_Theme_Text)
                    )
                    TextButton(onClick = { authState.toggleAuthMode() }) {
                        Text(if (authState.isSignUpScreen) "Login" else "Sign Up")
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

            }
        }
    }
}

//@Preview
//@Composable
//fun DefaultPreviewOfSignUpScreen() {
//    SignUpScreen()
//}