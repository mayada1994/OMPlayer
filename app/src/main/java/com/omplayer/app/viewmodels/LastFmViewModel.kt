package com.omplayer.app.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.omplayer.app.entities.LastFmSessionWrapper
import com.omplayer.app.repositories.LastFmRepository
import com.omplayer.app.utils.PreferenceUtil
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest

class LastFmViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "LastFmViewModel"
    private val API_KEY = "2f8cf16e71b4979ee0a4d2a692ed1206"
    private var SECRET = "9cc8988a83f3cbac07a6709c10361cd1"
    private val lastFmRepository = LastFmRepository()

    fun logIn(username: String, password: String) {
        val apiSignature: String =
            "api_key" + API_KEY + "methodauth.getMobileSessionpassword" + password + "username" + username + SECRET
        lastFmRepository.getLastFmSession(API_KEY, password, username, md5(apiSignature)!!)
            .enqueue(object : Callback<LastFmSessionWrapper> {

                override fun onResponse(call: Call<LastFmSessionWrapper>, response: Response<LastFmSessionWrapper>) {
                    try {
                        PreferenceUtil.currentLastFmSession = response.body()!!.session
                    } catch (e: Exception) {
                        Log.d(TAG, e.message)
                    }
                }

                override fun onFailure(call: Call<LastFmSessionWrapper>, t: Throwable) {
                    Log.d(TAG, t.message)
                }

            })
    }

    fun updatePlayingTrack(album: String, artist: String, track: String) {

        val apiSignature: String =
            "album" + album + "api_key" + API_KEY + "artist" + artist + "methodtrack.updateNowPlaying" + "sk" + PreferenceUtil.currentLastFmSession!!.key + "track" + track + SECRET
        lastFmRepository.updatePlayingTrack(
            album,
            artist,
            track,
            API_KEY,
            md5(apiSignature)!!,
            PreferenceUtil.currentLastFmSession!!.key
        ).enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d(TAG, response.body()!!.string())
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(TAG, t.message)
            }

        })
    }

    private fun md5(s: String): String? {
        val digest = MessageDigest.getInstance("MD5")
        try {
            val bytes = digest.digest(s.toByteArray(charset("UTF-8")))
            val b = StringBuilder(32)
            for (aByte in bytes) {
                val hex = Integer.toHexString(aByte.toInt() and 0xFF)
                if (hex.length == 1)
                    b.append('0')
                b.append(hex)
            }
            return b.toString()
        } catch (e: Exception) {
            Log.d(TAG, e.message)
        }
        return null
    }

}