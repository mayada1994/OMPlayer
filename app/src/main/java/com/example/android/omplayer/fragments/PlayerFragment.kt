package com.example.android.omplayer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.android.omplayer.R
import com.example.android.omplayer.activities.MainActivity
import com.example.android.omplayer.viewmodels.PlayerViewModel
import kotlinx.android.synthetic.main.fragment_player.*
import me.tankery.lib.circularseekbar.CircularSeekBar


class PlayerFragment : Fragment(), View.OnClickListener {

    private val viewModel: PlayerViewModel by lazy {
        ViewModelProviders.of(this).get(PlayerViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity)
            .setActionBarTitle("Player Fragment")
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        return inflater.inflate(R.layout.fragment_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycle.addObserver(viewModel)
        super.onViewCreated(view, savedInstanceState)

        initializeSeekbar()

        initializeTrackInfo()

        button_previous.setOnClickListener(this)
        button_next.setOnClickListener(this)
        button_reset.setOnClickListener(this)
        button_pause.setOnClickListener(this)
        button_play.setOnClickListener(this)

        viewModel.metadata.observe(this, Observer {
            it?.let { metadata ->
                seekbar_audio.max = metadata.duration.toFloat()
            }
        })
        viewModel.currentPosition.observe(this, Observer {
            seekbar_audio.progress = it?.toFloat() ?: (0).toFloat()
        })

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.button_play -> viewModel.onPlayClicked()
            R.id.button_pause -> viewModel.onPauseClicked()
            R.id.button_next -> {
                viewModel.onNextClicked()
                viewModel.loadTrackData(iv_track_cover, tv_track_title, tv_track_album, tv_track_artist, context!!)
            }
            R.id.button_previous -> {
                viewModel.onPrevClicked()
                viewModel.loadTrackData(iv_track_cover, tv_track_title, tv_track_album, tv_track_artist, context!!)
            }
            R.id.button_reset -> viewModel.onStopClicked()
        }
    }

    //region Initializing seekbar


    private fun initializeSeekbar() {

        seekbar_audio.setOnSeekBarChangeListener(object : CircularSeekBar.OnCircularSeekBarChangeListener {

            var userSelectedPosition = 0


            override fun onStartTrackingTouch(seekBar: CircularSeekBar) {

            }

            override fun onProgressChanged(circularSeekBar: CircularSeekBar, progress: Float, fromUser: Boolean) {
                if (fromUser) {
                    userSelectedPosition = progress.toInt()
                }
            }

            override fun onStopTrackingTouch(seekBar: CircularSeekBar) {
                viewModel.onSeek(userSelectedPosition)
            }

        })
    }

    fun initializeTrackInfo() {
        viewModel.loadTrackData(iv_track_cover, tv_track_title, tv_track_album, tv_track_artist, context!!)
    }


    //endregion

}
