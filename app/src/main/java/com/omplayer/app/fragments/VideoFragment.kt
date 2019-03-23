package com.omplayer.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import com.omplayer.app.R
import com.omplayer.app.activities.MainActivity
import com.omplayer.app.utils.LibraryUtil
import kotlinx.android.synthetic.main.fragment_video.*


class VideoFragment : Fragment() {

    private val YOUTUBE_API_KEY = "AIzaSyAcNnSv7GshAIBXOhYrXN_twsgMrt5J0Jc"
    private val VIDEO_ID = LibraryUtil.selectedTrackVideoId
    private val NOT_FOUND = "Not Found"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_video, container, false)
        (activity as MainActivity)
            .setActionBarTitle("Video")
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
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
        super.onViewCreated(view, savedInstanceState)
        if(VIDEO_ID != NOT_FOUND) {
            initializeYoutubeFragment()
        }else{
            video_view.visibility = View.GONE
            video_not_found.visibility = View.VISIBLE
        }
    }
}
