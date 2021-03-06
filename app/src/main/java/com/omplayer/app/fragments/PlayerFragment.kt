package com.omplayer.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.omplayer.app.R
import com.omplayer.app.activities.MainActivity
import com.omplayer.app.di.SingletonHolder
import com.omplayer.app.stateMachine.states.PausedState
import com.omplayer.app.stateMachine.states.PlayingState
import com.omplayer.app.utils.FormatUtils
import com.omplayer.app.viewmodels.LyricsViewModel
import com.omplayer.app.viewmodels.PlayerViewModel
import com.omplayer.app.viewmodels.TrackViewModel
import com.omplayer.app.viewmodels.VideoViewModel
import com.savantech.seekarc.SeekArc
import kotlinx.android.synthetic.main.fragment_player.*


class PlayerFragment : Fragment(), View.OnClickListener {

    private val viewModel: PlayerViewModel by lazy {
        ViewModelProviders.of(this).get(PlayerViewModel::class.java)
    }

    private val lyricsViewModel = LyricsViewModel(SingletonHolder.application)
    private val videoViewModel = VideoViewModel(SingletonHolder.application)
    private val trackViewModel = TrackViewModel(SingletonHolder.application)



    private var isPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity)
            .supportActionBar?.show()
        (activity as MainActivity)
            .setActionBarTitle(getString(R.string.action_bar_player))
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        return inflater.inflate(R.layout.fragment_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycle.addObserver(viewModel)
        super.onViewCreated(view, savedInstanceState)

        viewModel.scrobbleCachedTracks()

        initializeSeekbar()

        initializeTrackInfo()

        button_edit_tags.setOnLongClickListener {
            viewModel.loadCoverFromLastFm(
                tv_track_title.text.toString(),
                tv_track_artist.text.toString(),
                tv_track_album.text.toString(),
                iv_track_cover,
                true
            )
            true
        }


        button_previous.setOnClickListener(this)
        button_next.setOnClickListener(this)
        button_play.setOnClickListener(this)
        button_youtube_player.setOnClickListener(this)
        button_shuffle.setOnClickListener(this)
        button_lyrics.setOnClickListener(this)
        button_favorites.setOnClickListener(this)
        button_lastfm_love.setOnClickListener(this)
        button_lastfm_similar.setOnClickListener(this)
        button_edit_tags.setOnClickListener(this)

        viewModel.metadata.observe(this, Observer {
            it?.let { metadata ->
                seekbar_audio.setMaxProgress(metadata.duration.toFloat())
                timer_total.text = FormatUtils.millisecondsToString(metadata.duration.toLong())
                initializeTrackInfo()
            }
        })

        viewModel.favoriteMode.observe(this, Observer {
            if (it) {
                button_favorites.setImageResource(R.drawable.ic_favorite)
            } else {
                button_favorites.setImageResource(R.drawable.ic_favorite_border)
            }
        })

        viewModel.shuffleMode.observe(this, Observer {
            if (it != null) {
                button_shuffle.setImageResource(it)
            }
        })

        viewModel.currState.observe(this, Observer {
            when (it) {
                is PlayingState -> {
                    button_play.setImageResource(R.drawable.ic_pause_circle)
                    isPlaying = true
                }
                is PausedState -> {
                    button_play.setImageResource(R.drawable.ic_play_circle)
                    isPlaying = false
                }
            }
        })
        viewModel.currentPosition.observe(this, Observer {
            seekbar_audio.progress = it?.toFloat() ?: (0).toFloat()
            timer_played.text = it?.toLong()?.let { it1 -> FormatUtils.millisecondsToString(it1) }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.scrobbleCachedTracks()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.button_play -> {
                if (!isPlaying) {
                    button_play.setImageResource(R.drawable.ic_pause_circle)
                    viewModel.onPlayClicked()
                    isPlaying = true
                } else {
                    button_play.setImageResource(R.drawable.ic_play_circle)
                    viewModel.onPauseClicked()
                    isPlaying = false
                }
            }
            R.id.button_next -> viewModel.onNextClicked()
            R.id.button_previous -> viewModel.onPrevClicked()
            R.id.button_youtube_player -> videoViewModel.playVideo(fragmentManager!!)

            R.id.button_shuffle -> viewModel.onSetRepeatShuffleMode()
            R.id.button_lyrics -> lyricsViewModel.getSongLyrics(
                tv_track_artist.text.toString(),
                tv_track_title.text.toString(),
                fragmentManager!!
            )
            R.id.button_favorites -> viewModel.onFavoritesButtonClicked(viewModel.favoriteMode.value!!)
            R.id.button_lastfm_love -> viewModel.loveTrack(
                tv_track_artist.text.toString(),
                tv_track_title.text.toString()
            )
            R.id.button_lastfm_similar -> trackViewModel.loadSimilarTracks(
                tv_track_title.text.toString(),
                tv_track_artist.text.toString(),
                this@PlayerFragment
            )
            R.id.button_edit_tags -> viewModel.loadCoverFromLastFm(
                tv_track_title.text.toString(),
                tv_track_artist.text.toString(),
                tv_track_album.text.toString(),
                iv_track_cover,
                false
            )
        }
    }

    //region Initializing seekbar

    private fun initializeSeekbar() {
        seekbar_audio.setThumbRadius(8)

        seekbar_audio.setOnSeekArcChangeListener(object : SeekArc.OnSeekArcChangeListener {
            var userSelectedPosition = 0
            override fun onStartTrackingTouch(seekArc: SeekArc?) {
                viewModel.stopUpdateSeekbar()
            }

            override fun onProgressChanged(seekArc: SeekArc?, progress: Float) {
                userSelectedPosition = progress.toInt()
            }

            override fun onStopTrackingTouch(seekArc: SeekArc?) {
                viewModel.onSeek(userSelectedPosition)
                viewModel.startUpdateSeekbar()
            }
        })
    }

    private fun initializeTrackInfo() {
        viewModel.loadTrackData(iv_track_cover, tv_track_title, tv_track_album, tv_track_artist, context!!)
        viewModel.updateLastFmTrack()
    }

    //endregion

    override fun onDestroy() {
        super.onDestroy()
//        foreverObservers.forEach { it.release() }
//        scheduledTask?.cancel(true)
//        scheduledTask = null
    }

    fun openSimilarTracks() {
        view!!.findNavController().navigate(R.id.action_playerFragment_to_similarTracksFragment)
    }
}
