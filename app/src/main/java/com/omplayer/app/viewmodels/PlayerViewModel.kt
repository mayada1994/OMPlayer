package com.omplayer.app.viewmodels

import android.app.Application
import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.omplayer.app.R
import com.omplayer.app.db.entities.Track
import com.omplayer.app.di.SingletonHolder
import com.omplayer.app.utils.LibraryUtil
import com.omplayer.app.extensions.foreverObserver
import com.omplayer.app.livedata.ForeverObserver
import com.omplayer.app.stateMachine.Action
import com.omplayer.app.stateMachine.PlayerManager
import com.omplayer.app.stateMachine.states.IdleState
import com.omplayer.app.stateMachine.states.PlayingState
import com.mikhaellopez.circularimageview.CircularImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class PlayerViewModel(application: Application) : AndroidViewModel(application), LifecycleObserver {

    private val videoViewModel = VideoViewModel(application)

    private val playlist: MutableList<Track> = LibraryUtil.tracklist
    private val playerManager: PlayerManager = SingletonHolder.playerManager

    private val foreverObservers = mutableListOf<ForeverObserver<*>>()

    //region LiveData

    private val _currentPosition = MutableLiveData<Int?>()
    val currentPosition: LiveData<Int?> = _currentPosition

    private val _metadata = MediatorLiveData<Track?>().apply {
        addSource(playerManager.metadata) { value = it }
    }
    val metadata: LiveData<Track?> = _metadata
    //endregion

    private val executor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
    private val seekbarPositionUpdateTask: () -> Unit = {
        Handler(Looper.getMainLooper()).post {
            _currentPosition.value = playerManager.mediaPlayer.currentPosition
        }
    }
    private var scheduledTask: ScheduledFuture<*>? = null

    init {
        foreverObservers.add(
            playerManager.currState.foreverObserver(Observer {
                when (it) {
                    is PlayingState -> {
                        startUpdateSeekbar()
                    }
                    is IdleState -> {
                        _currentPosition.value = 0
                    }
                    else -> {
                        stopUpdateSeekbar()
                    }
                }
            })
        )
    }

    override fun onCleared() {
        super.onCleared()
        foreverObservers.forEach { it.release() }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        playerManager.setPlaylist(playlist, LibraryUtil.action)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        stopUpdateSeekbar()
    }

    private fun startUpdateSeekbar() {
        if (scheduledTask == null) {
            scheduledTask = executor.scheduleAtFixedRate(
                seekbarPositionUpdateTask,
                0,
                1,
                TimeUnit.SECONDS
            )
        }
    }

    private fun stopUpdateSeekbar() {
        scheduledTask?.cancel(true)
        scheduledTask = null
    }

    fun loadTrackData(cover: CircularImageView, title: TextView, album: TextView, artist: TextView, context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(coroutineContext) {
                val currentTrack = LibraryUtil.tracklist[LibraryUtil.selectedTrack]
                val currentAlbum = SingletonHolder.db.albumDao().getAlbumById(currentTrack.albumId)
                val currentArtist = SingletonHolder.db.artistDao().getArtistById(currentAlbum.artistId)
                withContext(Dispatchers.Main) {
                    videoViewModel.getVideoId(
                        currentArtist.name,
                        currentAlbum.title,
                        currentTrack.title
                    )
                    title.text = currentTrack.title
                    album.text = currentAlbum.title
                    artist.text = currentArtist.name
                    loadImage(currentAlbum.cover, cover, context)
                }
            }
        }

    }

    fun loadImage(albumArtUrl: String, cover: CircularImageView, context: Context) {
        val file = File(albumArtUrl)
        val uri = Uri.fromFile(file)

        Glide.with(context).load(uri)
            .apply(RequestOptions().placeholder(R.drawable.placeholder).error(R.drawable.placeholder))
            .into(cover)
    }

    //region View interaction

    fun onPlayClicked() = playerManager.performAction(Action.Play())

    fun onPauseClicked() = playerManager.performAction(Action.Pause())

    fun onNextClicked() = playerManager.performAction(Action.Next())

    fun onPrevClicked() = playerManager.performAction(Action.Prev())

    fun onStopClicked() = playerManager.performAction(Action.Stop())

    fun onSeek(position: Int) = playerManager.seekTo(position)
    //endregion
}