package com.omplayer.app.stateMachine.states

import com.omplayer.app.entities.TrackMetadata
import com.omplayer.app.stateMachine.Action
import com.omplayer.app.stateMachine.PlayerContext

class IdleState(context: PlayerContext) : State(context) {

    override fun handleAction(action: Action): State = when (action) {
        is Action.Play -> {
            val assetFileDescriptor = context.context.resources.openRawResourceFd(context.playlist[0])
            try {
                context.mediaPlayer.setDataSource(assetFileDescriptor.fileDescriptor, assetFileDescriptor.startOffset, assetFileDescriptor.declaredLength)
                context.mediaPlayer.prepare()
            } catch (e: Exception) {

            }
            context.updateMetadata(TrackMetadata(context.mediaPlayer.duration))
            context.mediaPlayer.start()
            assetFileDescriptor.close()

            PlayingState(context, 0)
        }
        else -> {
            this}
    }
}