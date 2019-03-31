package com.omplayer.app.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.omplayer.app.R
import com.omplayer.app.fragments.*

class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
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

    fun selectGenre(){
        val playerTransaction = supportFragmentManager.beginTransaction()
        playerTransaction.replace(R.id.fragment_placeholder, SingleGenreFragment()).addToBackStack(null)
        playerTransaction.commit()
    }

    fun selectArtist(){
        val playerTransaction = supportFragmentManager.beginTransaction()
        playerTransaction.replace(R.id.fragment_placeholder, SingleArtistFragment()).addToBackStack(null)
        playerTransaction.commit()
    }

    fun selectAlbum(){
        val playerTransaction = supportFragmentManager.beginTransaction()
        playerTransaction.replace(R.id.fragment_placeholder, SingleAlbumFragment()).addToBackStack(null)
        playerTransaction.commit()
    }

}