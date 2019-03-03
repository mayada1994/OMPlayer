package com.example.android.omplayer.stateMachine

import android.content.Context
import android.media.MediaPlayer
import com.example.android.omplayer.entities.TrackMetadata

interface PlayerContext {
    val context: Context
    val mediaPlayer: MediaPlayer?
    val playlist: MutableList<String>

    fun updateMetadata(metadata: TrackMetadata)
}