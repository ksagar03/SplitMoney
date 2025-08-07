package com.example.splitmoney
import android.util.Log
import android.app.Application
import androidx.room.Room
import com.example.splitmoney.database.AppDatabase
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MySplitMoneyApplication : Application(){
    override  fun onCreate() {
        super.onCreate()
        try {
            FirebaseApp.initializeApp(this)
//            Log.d("MyApplication", "Firebase initialized successfully")
        } catch (e: Exception) {
            Log.e("MyApplication", "Error initializing Firebase", e)

        }

//        val database by lazy {
//            Room.databaseBuilder(
//                applicationContext,
//                AppDatabase::class.java,
//                "split-money.db"
//            ).build()
//        }

    }
}