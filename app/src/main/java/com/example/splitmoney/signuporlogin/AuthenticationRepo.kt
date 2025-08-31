package com.example.splitmoney.signuporlogin

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.ImeOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.splitmoney.screens.SplitMoneyViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

sealed class AuthStateInfo {
    data object Loading : AuthStateInfo()
    data object Authenticated : AuthStateInfo()
    data object Unauthenticated : AuthStateInfo()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthStateInfo>(AuthStateInfo.Loading)
    val authState: StateFlow<AuthStateInfo> get() = _authState


//    init {
//        startListeningToAuthState()
//    }

    private val authStateListener = FirebaseAuth.AuthStateListener { auth ->
        if (auth.currentUser != null) {
            _authState.value = AuthStateInfo.Authenticated
        } else {
            _authState.value = AuthStateInfo.Unauthenticated
        }
    }

    fun startListeningToAuthState() {
        auth.addAuthStateListener(this.authStateListener)
        // Check initial state
        if (auth.currentUser != null) {
            _authState.value = AuthStateInfo.Authenticated
        } else {
            _authState.value = AuthStateInfo.Unauthenticated
        }
    }

    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.message)
                }

            }
    }


    fun signUp(
        name: String,
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit,
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
//                onResult(true, null)
                    saveUserToFireStore(name, email, onResult)
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }


    private fun saveUserToFireStore(
        name: String,
        email: String,
        onResult: (Boolean, String?) -> Unit,
    ) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val user = hashMapOf(
                "name" to name,
                "email" to email
            )

            firestore.collection("users").document(userId)
                .set(user)
                .addOnSuccessListener {
                    onResult(true, null)
                }.addOnFailureListener { e ->
                    onResult(false, e.message)
                }
        } else {
            onResult(false, "User ID is null")
        }
    }


    fun logout() {
        auth.signOut()
    }

    override fun onCleared() {
        super.onCleared()
        auth.removeAuthStateListener(authStateListener)
    }

    fun keyboardOptions(isPasswordField: Boolean): KeyboardOptions {
        return if(!isPasswordField){

            KeyboardOptions(imeAction = ImeAction.Next)
        }else {
            KeyboardOptions(keyboardType = KeyboardType.Password)
        }



    }
    fun keyboardActions(focusRequester: FocusRequester): KeyboardActions {
        return KeyboardActions(onNext = {
            focusRequester.requestFocus()})
    }




}




