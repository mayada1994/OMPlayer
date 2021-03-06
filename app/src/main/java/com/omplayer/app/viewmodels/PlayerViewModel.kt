package com.omplayer.app.viewmodels

import android.app.Application
import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mikhaellopez.circularimageview.CircularImageView
import com.omplayer.app.R
import com.omplayer.app.db.entities.ScrobbledTrack
import com.omplayer.app.db.entities.Track
import com.omplayer.app.di.SingletonHolder
import com.omplayer.app.extensions.foreverObserver
import com.omplayer.app.livedata.ForeverObserver
import com.omplayer.app.repositories.ScrobbledTrackRepository
import com.omplayer.app.stateMachine.Action
import com.omplayer.app.stateMachine.PlayerManager
import com.omplayer.app.stateMachine.states.IdleState
import com.omplayer.app.stateMachine.states.PlayingState
import com.omplayer.app.utils.LastFmUtil
import com.omplayer.app.utils.LibraryUtil
import com.omplayer.app.utils.NetworkUtil.networkEnabled
import com.omplayer.app.utils.PreferenceUtil
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

    companion object {
        val TAG: String = "PlayerViewModel"
        const val NORMAL_MODE = 0
        const val LOOP_MODE = 1
        const val SHUFFLE_MODE = 2
    }

    private val lastFmViewModel = LastFmViewModel(application)
    private val videoViewModel = VideoViewModel(application)

    private val scrobbledTrackRepository = ScrobbledTrackRepository(SingletonHolder.db.scrobbledTrackDao())

    private val playlist: List<Track> = LibraryUtil.tracklist
    private val playerManager: PlayerManager = SingletonHolder.playerManager

    private val foreverObservers = mutableListOf<ForeverObserver<*>>()

    private var mode = NORMAL_MODE

    //region MediaLibraryCompat

    private val mediaControllerCompat: MediaControllerCompat by lazy {
        MediaControllerCompat(
            application,
            playerManager.mediaSessionCompat.sessionToken
        ).apply {
            registerCallback(
                object : MediaControllerCompat.Callback() {

                    override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
                        super.onPlaybackStateChanged(state)
                        Log.d(TAG, "onPlaybackStateChanged: $state")
                    }
                }
            )
        }
    }

    //endregion

    //region LiveData

    private val _currentPosition = MutableLiveData<Int?>()
    val currentPosition: LiveData<Int?> = _currentPosition

    private val _shuffleMode = MutableLiveData<Int?>()
    val shuffleMode: LiveData<Int?> = _shuffleMode

    private val _favoriteMode = MutableLiveData<Boolean>()
    val favoriteMode: LiveData<Boolean> = _favoriteMode

    val currState = SingletonHolder.playerManager.currState

    private val _metadata = MediatorLiveData<Track?>().apply {
        addSource(playerManager.metadata) { value = it }
    }
    val metadata: LiveData<Track?> = _metadata
    //endregion

    //region SeekBarUpdate

    private val executor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
    private val seekbarPositionUpdateTask: () -> Unit = {
        Handler(Looper.getMainLooper()).post {

            if (playerManager.mediaPlayer.currentPosition > 20000000) {
                _currentPosition.value = 0
            } else {
                _currentPosition.value = playerManager.mediaPlayer.currentPosition
            }
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


    fun startUpdateSeekbar() {
        if (scheduledTask == null) {
            scheduledTask = executor.scheduleAtFixedRate(
                seekbarPositionUpdateTask,
                0,
                1,
                TimeUnit.SECONDS
            )
        }
    }

    fun stopUpdateSeekbar() {
        scheduledTask?.cancel(true)
        scheduledTask = null
    }

    //endregion

    override fun onCleared() {
        super.onCleared()
        foreverObservers.forEach { it.release() }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        onSetRepeatShuffleMode()
        playerManager.setPlaylist(playlist)
        when (LibraryUtil.action) {
            is Action.Play -> {
                onPlayClicked()
            }
            is Action.Pause -> {
                onPauseClicked()
            }
        }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        startUpdateSeekbar()
        Log.d(TAG, LibraryUtil.selectedTrack.toString())
}


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        stopUpdateSeekbar()
    }


    fun loadTrackData(cover: CircularImageView, title: TextView, album: TextView, artist: TextView, context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(coroutineContext) {
                val currentTrack = LibraryUtil.tracklist[LibraryUtil.selectedTrack]
                val currentAlbum = SingletonHolder.db.albumDao().getAlbumById(currentTrack.albumId)
                val currentArtist = SingletonHolder.db.artistDao().getArtistById(currentTrack.artistId)
                withContext(Dispatchers.Main) {
                    videoViewModel.getVideoId(
                        currentArtist.name,
                        currentAlbum.title,
                        currentTrack.title
                    )
                    title.text = currentTrack.title
                    album.text = currentAlbum.title
                    artist.text = currentArtist.name
                    _favoriteMode.value = currentTrack.favorite
                    loadImage(currentAlbum.cover, cover, context)
                }
            }
        }

    }

    private fun loadImage(albumArtUrl: String, cover: CircularImageView, context: Context) {
        val file = File(albumArtUrl)
        val uri = Uri.fromFile(file)

        Glide.with(context).load(uri)
            .apply(RequestOptions().placeholder(R.drawable.placeholder).error(R.drawable.placeholder))
            .into(cover)
    }

    fun updateLastFmTrack() {
        if (PreferenceUtil.scrobble && networkEnabled) {
            CoroutineScope(Dispatchers.IO).launch {
                withContext(coroutineContext) {
                    val currentTrack = LibraryUtil.tracklist[LibraryUtil.selectedTrack]
                    val currentAlbum = SingletonHolder.db.albumDao().getAlbumById(currentTrack.albumId).title
                    val currentArtist = SingletonHolder.db.artistDao().getArtistById(currentTrack.artistId).name
                    lastFmViewModel.updatePlayingTrack(currentAlbum, currentArtist, currentTrack.title)
                }
            }
        }
    }

    fun scrobbleTrack(timestamp: String) {
        if (PreferenceUtil.scrobble) {
            CoroutineScope(Dispatchers.IO).launch {
                withContext(coroutineContext) {
                    val currentTrack = LibraryUtil.tracklist[LibraryUtil.selectedTrack]
                    val currentAlbum = SingletonHolder.db.albumDao().getAlbumById(currentTrack.albumId).title
                    val currentArtist = SingletonHolder.db.artistDao().getArtistById(currentTrack.artistId).name
                    if (networkEnabled) {
                        lastFmViewModel.scrobble(currentAlbum, currentArtist, currentTrack.title, timestamp)
                    } else {
                        Log.d("LastFm", "cached")
                        scrobbledTrackRepository.insertTrack(
                            ScrobbledTrack(
                                currentAlbum,
                                currentArtist,
                                currentTrack.title,
                                timestamp
                            )
                        )
                    }
                }
            }
        }
    }

    fun scrobbleCachedTracks() {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(coroutineContext) {
                scrobbledTrackRepository.getAllScrobbledTracks()?.forEach {
                    if (networkEnabled) {
                        lastFmViewModel.scrobble(it.albumTitle, it.artistTitle, it.title, it.timestamp)
                        scrobbledTrackRepository.deleteTrack(it)
                    }
                }
            }
        }
    }

    fun loadCoverFromLastFm(title: String, artist: String, album: String, albumImageView: CircularImageView, load: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(coroutineContext) {
                val currentTrack = LibraryUtil.tracklist[LibraryUtil.selectedTrack]
                val currentAlbum = SingletonHolder.db.albumDao().getAlbumById(currentTrack.albumId)
                withContext(Dispatchers.Main) {
                    if(currentAlbum.cover.isEmpty() || load)
                    lastFmViewModel.getTrackInfo(title, artist, album, albumImageView, currentAlbum, load)
                }
            }
        }

    }

    //region View interaction

    fun onPlayClicked() = mediaControllerCompat.transportControls.play()

    fun onPauseClicked() = mediaControllerCompat.transportControls.pause()

    fun onStopClicked() = mediaControllerCompat.transportControls.stop()

    fun onSeek(position: Int) = mediaControllerCompat.transportControls.seekTo(position.toLong())

    fun onNextClicked() {
        _currentPosition.value = 0
        mediaControllerCompat.transportControls.skipToNext()
    }

    fun onPrevClicked() {
        _currentPosition.value = 0
        mediaControllerCompat.transportControls.skipToPrevious()
    }

    fun onSetRepeatShuffleMode() {

        when (mode) {

            NORMAL_MODE -> {
                mediaControllerCompat.transportControls.setRepeatMode(PlaybackStateCompat.REPEAT_MODE_NONE)
                mediaControllerCompat.transportControls.setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_NONE)
                _shuffleMode.value = R.drawable.ic_repeat
                mode = LOOP_MODE
            }
            LOOP_MODE -> {
                mediaControllerCompat.transportControls.setRepeatMode(PlaybackStateCompat.REPEAT_MODE_ONE)
                _shuffleMode.value = R.drawable.ic_repeat_one
                mode = SHUFFLE_MODE
            }
            SHUFFLE_MODE -> {
                mediaControllerCompat.transportControls.setRepeatMode(PlaybackStateCompat.REPEAT_MODE_NONE)
                mediaControllerCompat.transportControls.setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_ALL)
                _shuffleMode.value = R.drawable.ic_shuffle
                mode = NORMAL_MODE

            }

        }
    }

    fun onFavoritesButtonClicked(isFavorite: Boolean) {
        val currentTrack = LibraryUtil.tracklist[LibraryUtil.selectedTrack]
        if (isFavorite) {
            currentTrack.favorite = false
            _favoriteMode.value = false
        } else {
            currentTrack.favorite = true
            _favoriteMode.value = true
        }
        CoroutineScope(Dispatchers.IO).launch {
            SingletonHolder.db.trackDao().update(currentTrack)
        }
    }

    //endregion

    fun scrollableTrack(playedTime: Int, timestamp: Long): Boolean {
        val currentTrack = LibraryUtil.tracklist[LibraryUtil.selectedTrack]
        if (currentTrack.duration >= 30000) {
            return if (currentTrack.duration / 2 > 400000) {
                (playedTime >= 400000) || (LastFmUtil.timestamp() - timestamp >= 240)
            } else {
                (playedTime >= currentTrack.duration / 2) || (LastFmUtil.timestamp() - timestamp >= currentTrack.duration / 2)
            }
        }
        return false
    }

    fun loveTrack(artist: String, track: String) {
        if (networkEnabled) {
            if(PreferenceUtil.currentLastFmSession!=null) {
                lastFmViewModel.loveTrack(artist, track)
            }else{
                Toast.makeText(getApplication(), "Please login to Last.fm", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(getApplication(), "No network connection", Toast.LENGTH_LONG).show()
        }
    }
}