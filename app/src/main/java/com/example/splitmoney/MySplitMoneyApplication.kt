package com.example.splitmoney
import android.util.Log
import android.app.Application
import com.google.firebase.FirebaseApp

class MySplitMoneyApplication : Application(){
    override  fun onCreate() {
        super.onCreate()
        try {
            FirebaseApp.initializeApp(this)
//            Log.d("MyApplication", "Firebase initialized successfully")
        } catch (e: Exception) {
            Log.e("MyApplication", "Error initializing Firebase", e)

        }
    }
}