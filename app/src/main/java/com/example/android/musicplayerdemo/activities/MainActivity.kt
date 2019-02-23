package com.example.android.musicplayerdemo.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.android.musicplayerdemo.fragments.MainFragment
import com.example.android.musicplayerdemo.R

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