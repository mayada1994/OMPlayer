package com.omplayer.app.dialogFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import com.omplayer.app.R
import com.omplayer.app.utils.LibraryUtil


class VideoDialogFragment : DialogFragment() {

    private val YOUTUBE_API_KEY = "AIzaSyAcNnSv7GshAIBXOhYrXN_twsgMrt5J0Jc"
    private val VIDEO_ID = LibraryUtil.selectedTrackVideoId

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_video, container, false)
        dialog.setCanceledOnTouchOutside(true)
        return view
    }

    private var mYoutubePlayer: YouTubePlayer? = null

    private fun initializeYoutubeFragment() {

        val youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance()

        youTubePlayerFragment.initialize(YOUTUBE_API_KEY, object : YouTubePlayer.OnInitializedListener {

            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider,
                player: YouTubePlayer,
                wasRestored: Boolean
            ) {
                if (!wasRestored) {
                    mYoutubePlayer = player
                    mYoutubePlayer!!.setManageAudioFocus(true)
                    mYoutubePlayer!!.setShowFullscreenButton(false)
                    mYoutubePlayer!!.cueVideo(VIDEO_ID)
                }

            }

            override fun onInitializationFailure(arg0: YouTubePlayer.Provider, arg1: YouTubeInitializationResult) {


            }
        })
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.video_view, youTubePlayerFragment as Fragment).commit()
    }


    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        initializeYoutubeFragment()
    }

    companion object {

        fun newInstance(): VideoDialogFragment {
            val videoDialogFragment = VideoDialogFragment()
            videoDialogFragment.setStyle(STYLE_NO_TITLE, 0)
            return videoDialogFragment
        }
    }
}
