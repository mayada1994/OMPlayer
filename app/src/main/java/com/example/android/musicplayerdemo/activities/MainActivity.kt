package com.example.android.musicplayerdemo.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.android.musicplayerdemo.MainViewModel
import com.example.android.musicplayerdemo.R
import com.example.android.musicplayerdemo.stateMachine.Action.*
import com.example.android.musicplayerdemo.stateMachine.PlayerStateMachine
import com.example.android.musicplayerdemo.stateMachine.SeekbarCallback
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener, SeekbarCallback {

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    companion object {
        const val MEDIA_RES_1 = R.raw.funky_town
        const val MEDIA_RES_2 = R.raw.the_man_who
    }

    private var playlist: MutableList<Int> = ArrayList()
    private var mUserIsSeeking = false
    private val playerSM: PlayerStateMachine = PlayerStateMachine(this, this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeSeekbar()

        playlist.add(MEDIA_RES_1)
        playlist.add(MEDIA_RES_2)

        button_previous.setOnClickListener(this)
        button_next.setOnClickListener(this)
        button_reset.setOnClickListener(this)
        button_pause.setOnClickListener(this)
        button_play.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()
        playerSM.setPlaylist(playlist, Pause())

    }

    override fun onStop() {
        super.onStop()
        playerSM.release()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.button_play -> playerSM.performAction(Play())
            R.id.button_pause -> playerSM.performAction(Pause())
            R.id.button_next -> playerSM.performAction(Next())
            R.id.button_previous -> playerSM.performAction(Prev())
            R.id.button_reset -> playerSM.performAction(Stop())
        }
    }


    //region Initializing elements


    private fun initializeSeekbar() {
        seekbar_audio.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                var userSelectedPosition = 0

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    mUserIsSeeking = true
                }

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        userSelectedPosition = progress
                    }
                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    mUserIsSeeking = false
                    playerSM.seekTo(userSelectedPosition)
                }
            })
    }
    //endregion

    //region Seekbar Callback

    override fun updateSeekbarPosition(position: Int) {
        if (!mUserIsSeeking) {
            seekbar_audio.setProgress(position, true)
        }

    }

    override fun updateSeekbarDuration(duration: Int) {
        seekbar_audio.max = duration
    }

    //endregion


}
