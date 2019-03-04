package com.example.android.musicplayerdemo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.android.musicplayerdemo.R
import com.example.android.musicplayerdemo.activities.MainActivity
import com.example.android.musicplayerdemo.viewmodels.PlayerViewModel
import kotlinx.android.synthetic.main.fragment_player.*


class PlayerFragment : Fragment(), View.OnClickListener {

    private val viewModel: PlayerViewModel by lazy {
        ViewModelProviders.of(this).get(PlayerViewModel::class.java)
    }

    private var isPlaying = false

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

        button_previous.setOnClickListener(this)
        button_next.setOnClickListener(this)
        button_play.setOnClickListener(this)

        viewModel.metadata.observe(this, Observer {
            it?.let { metadata ->
                seekbar_audio.max = metadata.duration
            }
        })
        viewModel.currentPosition.observe(this, Observer {
            seekbar_audio.setProgress(it ?: 0, true)
        })

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.button_play -> {
                if (!isPlaying) {
                    button_play.setImageResource(R.drawable.pause)
                    viewModel.onPlayClicked()
                    isPlaying = true
                } else {
                    button_play.setImageResource(R.drawable.play)
                    viewModel.onPauseClicked()
                    isPlaying = false
                }
            }
            R.id.button_next -> viewModel.onNextClicked()
            R.id.button_previous -> viewModel.onPrevClicked()
        }
    }

    //region Initializing seekbar


    private fun initializeSeekbar() {
        seekbar_audio.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                var userSelectedPosition = 0

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                }

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        userSelectedPosition = progress
                    }
                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    viewModel.onSeek(userSelectedPosition)
                }
            })
    }

    //endregion


}
