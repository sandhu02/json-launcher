package com.awais.jsonlauncher.repositories

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject
import javax.inject.Singleton

data class NetworkState(
    val wifiConnected: Boolean = false,
    val mobileConnected: Boolean = false,
    val connectionStatus: Boolean = wifiConnected or mobileConnected
)

@Singleton
class NetworkRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun networkState(): Flow<NetworkState> = callbackFlow {
        val checkState = {
            val state = getNetworkState()
            Log.d("WifiRepository", "Network State: $state")
            trySend(state)
        }

        checkState()

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                checkState()
            }
        }

        val filter = IntentFilter().apply {
            addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(receiver, filter, Context.RECEIVER_EXPORTED)
        } else {
            context.registerReceiver(receiver, filter)
        }

        awaitClose {
            try {
                context.unregisterReceiver(receiver)
            } catch (e: Exception) {
                Log.e("WifiRepository", "Error unregistering receiver", e)
            }
        }
    }.distinctUntilChanged()

    private fun getNetworkState(): NetworkState {
        var wifiConnected = false
        var mobileConnected = false

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                connectivityManager.allNetworks.forEach { network ->
                    val capabilities = connectivityManager.getNetworkCapabilities(network)
                    capabilities?.let {
                        if (it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                            it.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {

                            when {
                                it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> wifiConnected = true
                                it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> mobileConnected = true
                            }
                        }
                    }
                }
            } else {
                @Suppress("DEPRECATION")
                connectivityManager.allNetworkInfo.forEach { info ->
                    if (info.isConnected) {
                        when (info.type) {
                            ConnectivityManager.TYPE_WIFI -> wifiConnected = true
                            ConnectivityManager.TYPE_MOBILE -> mobileConnected = true
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("WifiRepository", "Error getting network state", e)
        }

        return NetworkState(
            wifiConnected = wifiConnected,
            mobileConnected = mobileConnected,
            connectionStatus = wifiConnected or mobileConnected
        )
    }
}