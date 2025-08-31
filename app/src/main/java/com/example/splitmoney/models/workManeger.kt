import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject
import javax.inject.Singleton

//package com.example.splitmoney.models
//
//import android.content.Context
//import androidx.work.CoroutineWorker
//import androidx.work.WorkerParameters
//import com.example.splitmoney.MySplitMoneyApplication
//import com.example.splitmoney.firebase.SyncRepository
//import javax.inject.Inject
//
//class SyncWorker(
//    context: Context,
//    params: WorkerParameters,
//): CoroutineWorker(context, params) {
//
//    @Inject
//    lateinit var syncRepository: SyncRepository
////   init {
////       (context.applicationContext as ).appComponent.inject(this)
////   }
//
//
//    override suspend fun doWork(): Result {
//        return try {
//            syncRepository.syncPendingOperations()
//        }
//    }
//}


@Singleton
class NetworkMonitor @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val isOnline: Flow<Boolean> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(true)
            }

            override fun onLost(network: Network) {
                trySend(false)

            }

            override fun onUnavailable() {
                trySend(false)
            }
        }
        val networkRequest =
            NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()

        connectivityManager.registerNetworkCallback(networkRequest, callback)

        val currentState = connectivityManager.activeNetwork?.let {
            network -> connectivityManager.getNetworkCapabilities(network)?.hasCapability(
            NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } ?: false

        trySend(currentState)

        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }

    }.distinctUntilChanged()
    // distinctUntilChanged() --> Returns flow where all subsequent repetitions of the same value are filtered out.
}