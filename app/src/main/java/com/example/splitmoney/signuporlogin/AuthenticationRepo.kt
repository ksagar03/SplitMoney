package com.example.splitmoney.signuporlogin

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

sealed class AuthStateInfo {
    data object Loading : AuthStateInfo()
    data object Authenticated : AuthStateInfo()
    data object Unauthenticated : AuthStateInfo()
}


class AuthViewModel : ViewModel() {
    private val _authState = MutableStateFlow<AuthStateInfo>(AuthStateInfo.Loading)
    val authState: StateFlow<AuthStateInfo> get() = _authState

    private  val firebaseAuth: FirebaseAuth by lazy{
        FirebaseAuth.getInstance()
    }
    private val firestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    private val authStateListener = FirebaseAuth.AuthStateListener { auth ->
        if (auth.currentUser != null) {
            _authState.value = AuthStateInfo.Authenticated
        } else {
            _authState.value = AuthStateInfo.Unauthenticated
        }
    }

    fun startListeningToAuthState() {
        firebaseAuth.addAuthStateListener(this.authStateListener)
        // Check initial state
        if (firebaseAuth.currentUser != null) {
            _authState.value = AuthStateInfo.Authenticated
        } else {
            _authState.value = AuthStateInfo.Unauthenticated
        }
    }

    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
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
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
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
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val user = hashMapOf(
                "name" to name,
                "email" to email
            )

            FirebaseFirestore.getInstance().collection("users").document(userId)
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
        FirebaseAuth.getInstance().signOut()
    }

    override fun onCleared() {
        super.onCleared()
        firebaseAuth.removeAuthStateListener(authStateListener)
    }


}


