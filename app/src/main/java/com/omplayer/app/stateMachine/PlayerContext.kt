package com.omplayer.app.stateMachine

import android.content.Context
import android.media.MediaPlayer
import com.omplayer.app.db.entities.Track

interface PlayerContext {
    val context: Context
    val mediaPlayer: MediaPlayer?
    val playlist: MutableList<Track>

    fun updateMetadata(metadata: Track)
}