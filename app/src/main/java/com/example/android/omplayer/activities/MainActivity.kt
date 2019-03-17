package com.example.android.omplayer.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.android.omplayer.fragments.MainFragment
import com.example.android.omplayer.R
import com.example.android.omplayer.fragments.PlayerFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragment_placeholder, MainFragment())
        transaction.commit()
    }

    fun setActionBarTitle(title: String) {
        supportActionBar!!.title = title
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun openPlayerFragment(){
        val playerTransaction = supportFragmentManager.beginTransaction()
        playerTransaction.replace(R.id.fragment_placeholder, PlayerFragment()).addToBackStack(null)
        playerTransaction.commit()
    }
}