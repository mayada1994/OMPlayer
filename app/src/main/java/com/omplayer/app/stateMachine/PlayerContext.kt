package com.omplayer.app.stateMachine

import android.content.Context
import android.media.MediaPlayer
import android.support.v4.media.session.MediaSessionCompat
import com.omplayer.app.entities.TrackMetadata

interface PlayerContext {
    val context: Context
    val mediaPlayer: MediaPlayer
    val playlist: MutableList<Int>
    val mediaSessionCompat : MediaSessionCompat

    fun updateMetadata(metadata: TrackMetadata)
}