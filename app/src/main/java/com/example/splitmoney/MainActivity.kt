package com.example.splitmoney
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.splitmoney.screens.Navigation
import com.example.splitmoney.screens.SplitMoneyViewModel
import com.example.splitmoney.signuporlogin.AuthViewModel
import com.example.splitmoney.ui.theme.gradient
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {

                AppContent()
        }

    }
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
private fun AppContent(){
    val splitMoneyViewModel: SplitMoneyViewModel = hiltViewModel()
    val authViewModel: AuthViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        authViewModel.startListeningToAuthState()
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(gradient)) {
        Navigation(viewModel = splitMoneyViewModel , authViewModel = authViewModel)
    }

}



