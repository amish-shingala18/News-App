package com.example.news.domain

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.lifecycle.MutableLiveData

class ApplicationNetwork : Application() {

    override fun onCreate() {
        checkInternet(this)
        super.onCreate()
    }
    val liveData = MutableLiveData<Boolean>()
    private fun checkInternet(context: Context){
        val connectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest,object :
            NetworkCallback(){
            override fun onAvailable(network: Network) {
                liveData.postValue(true)
                Log.d("TAG", "=====================onAvailable: Internet is On=====================")
            }
            override fun onLost(network: Network) {
                liveData.postValue(false)
                Log.d("TAG", "=====================onAvailable: Internet is Off=====================")
            }
        })
    }
}