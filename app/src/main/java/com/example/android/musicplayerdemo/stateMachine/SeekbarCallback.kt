package com.example.android.musicplayerdemo.stateMachine

interface SeekbarCallback {

        fun updateSeekbarDuration(duration: Int)
        fun updateSeekbarPosition(position: Int)
}