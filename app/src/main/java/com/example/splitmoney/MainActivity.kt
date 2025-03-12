package com.example.splitmoney

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.example.splitmoney.header.Header
import com.example.splitmoney.signuporlogin.AuthViewModel
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    private val splitMoneyViewModel: SplitMoneyViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    @SuppressLint("MissingSuperCall", "UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
//            SplitMoneyTheme {
//                Surface (color = MaterialTheme.colorScheme.background ){
//                    SplitMoneyAppView(viewModel = viewModel)
//
//                }
//            }
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = colorResource(id = R.color.Dark_Theme)
            ) {
                Navigation(viewModel = splitMoneyViewModel, authViewModel = authViewModel)

            }
        }

    }
}


//@Composable
//fun SplitMoneyAppView(splitMoneyViewModel: SplitMoneyViewModel, authViewModel: AuthViewModel) {
//    Navigation(viewModel = splitMoneyViewModel, authViewModel = authViewModel)
//
//}



