package com.example.android.omplayer.stateMachine.states

import com.example.android.omplayer.stateMachine.Action
import com.example.android.omplayer.stateMachine.PlayerContext

abstract class State(protected val context: PlayerContext) {

    abstract fun handleAction(action: Action) : State
}