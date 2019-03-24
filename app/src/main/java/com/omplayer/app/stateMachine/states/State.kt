package com.omplayer.app.stateMachine.states

import com.omplayer.app.stateMachine.Action
import com.omplayer.app.stateMachine.PlayerContext

abstract class State(protected val context: PlayerContext) {

    abstract fun handleAction(action: Action) : State
}