package com.omplayer.app.utils

import android.util.Log
import com.omplayer.app.entities.LastFmSimilarTrack
import java.security.MessageDigest

object LastFmUtil {

    var similarTracks = listOf<LastFmSimilarTrack>()
    var originalTrack = ""
    var isScrobbled: Boolean = false

    fun timestamp(): Long = (System.currentTimeMillis() / 1000)

    fun md5(s: String): String? {
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
            Log.d("LastFmUtil", e.message)
        }
        return null
    }
}