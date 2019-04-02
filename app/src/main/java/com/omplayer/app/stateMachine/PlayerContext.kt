package com.omplayer.app.stateMachine

import android.content.Context
import android.media.MediaPlayer
import android.support.v4.media.session.MediaSessionCompat
import com.omplayer.app.db.entities.Track

interface PlayerContext {
    val context: Context
    val mediaPlayer: MediaPlayer?
    val playlist: MutableList<Track>
    val mediaSessionCompat : MediaSessionCompat
    var isShuffle : Boolean
    var isLooping : Boolean

    fun updateMetadata(metadata: Track)
}