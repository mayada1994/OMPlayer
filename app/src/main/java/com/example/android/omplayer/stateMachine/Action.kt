package com.example.android.omplayer.stateMachine

import java.io.Serializable

sealed class Action : Serializable {
    class Play : Action()
    class Pause : Action()
    class Stop : Action()
    class Next : Action()
    class Prev : Action()
}