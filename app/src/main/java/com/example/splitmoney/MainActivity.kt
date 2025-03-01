package com.example.splitmoney

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.splitmoney.signuporlogin.AuthScreen
import com.example.splitmoney.ui.theme.SplitMoneyTheme

class MainActivity : ComponentActivity() {
    private val viewModel: SplitMoneyViewModel by viewModels()

    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            SplitMoneyTheme {
//                Surface (color = MaterialTheme.colorScheme.background ){
//                    SplitMoneyAppView(viewModel = viewModel)
//
//                }
//            }
            AuthScreen()
        }

    }
}


@Composable
fun SplitMoneyAppView(viewModel: SplitMoneyViewModel) {
    Navigation(viewModel = viewModel)

}



