package com.example.android.omplayer.stateMachine

import android.content.Context
import android.media.MediaPlayer
import com.example.android.omplayer.db.entities.Track

interface PlayerContext {
    val context: Context
    val mediaPlayer: MediaPlayer?
    val playlist: MutableList<Track>

    fun updateMetadata(metadata: Track)
}