package com.example.splitmoney.header

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.splitmoney.R
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(
    onLogoutClick: () -> Unit,
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(300)
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { -100 }) + fadeIn(animationSpec = tween(600))
    ) {

        TopAppBar(
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "app logo",
                        modifier = Modifier
                            .size(50.dp)
                            .alpha(0.9f)
                            .clip(RoundedCornerShape(50.dp))
                    )
                    IconButton(
                        onClick = onLogoutClick,
                        content = {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_logout_24),
                                contentDescription = "Logout",
                            )

                        }, colors = IconButtonColors(
                            containerColor = colorResource(id = R.color.Dark_Theme_Secondary),
                            contentColor = colorResource(id = R.color.Dark_Theme_alert),
                            disabledContainerColor = colorResource(id = R.color.teal_700),
                            disabledContentColor = colorResource(id = R.color.teal_700)
                        )
                    )
                }
            },
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = colorResource(id = R.color.Dark_Theme_Secondary)

            ),
            modifier = Modifier.fillMaxWidth()
        )

    }

}

//@Preview
//@Composable
//fun Uidisp(){
//    Header(onLogoutClick = {})
//}