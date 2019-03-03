package com.example.android.omplayer.stateMachine

sealed class Action {
    class Play : Action()
    class Pause : Action()
    class Stop : Action()
    class Next : Action()
    class Prev : Action()
}