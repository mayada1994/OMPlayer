package com.example.android.musicplayerdemo.stateMachine

sealed class Action {
    class Play : Action()
    class Pause : Action()
    class Stop : Action()
    class Next : Action()
    class Prev : Action()
}