package com.omplayer.app.activities

import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.omplayer.app.R
import com.omplayer.app.utils.NetworkUtil.wifiBroadcastReceiver
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION)
        registerReceiver(wifiBroadcastReceiver, intentFilter)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(wifiBroadcastReceiver)
    }

    fun setActionBarTitle(title: String) {
        supportActionBar!!.title = title
    }

    override fun onSupportNavigateUp() = findNavController(nav_host_fragment).navigateUp()
}