package com.omplayer.app.utils

import android.net.wifi.WifiManager
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context


object NetworkUtil {
    var networkEnabled: Boolean = true

    val wifiBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN)
            networkEnabled = wifiState != WifiManager.WIFI_STATE_DISABLED
        }
    }
}