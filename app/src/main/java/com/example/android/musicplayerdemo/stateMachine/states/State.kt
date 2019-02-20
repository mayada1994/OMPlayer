package com.example.android.musicplayerdemo.stateMachine.states

import com.example.android.musicplayerdemo.stateMachine.Action
import com.example.android.musicplayerdemo.stateMachine.PlayerContext

abstract class State(protected val context: PlayerContext) {

    abstract fun handleAction(action: Action) : State
}