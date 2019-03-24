package com.omplayer.app.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.omplayer.app.R
import com.omplayer.app.fragments.MainFragment

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
}