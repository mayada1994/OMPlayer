package com.omplayer.app.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.omplayer.app.R
import com.omplayer.app.di.SingletonHolder
import com.omplayer.app.viewmodels.EditTagsViewModel
import com.omplayer.app.viewmodels.LastFmViewModel
import kotlinx.android.synthetic.main.fragment_edit_tags.*

class EditTagsFragment : Fragment(), View.OnClickListener {

    val viewModel = EditTagsViewModel(SingletonHolder.application)
    private val lastFmViewModel = LastFmViewModel(SingletonHolder.application)

    var trackTitle = ""
    var trackAlbum = ""
    var trackArtist = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_tags, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.initViews(
            track_title,
            track_album,
            track_artist,
            track_position,
            track_genre
        )
        viewModel.loadTrackAlbumCover(track_cover)

        trackTitle = track_title.text.toString()
        trackAlbum = track_album.text.toString()
        trackArtist = track_artist.text.toString()

    }

    override fun onClick(v: View?) {
        when (v) {
            button_close -> fragmentManager!!.popBackStack()
            button_save -> saveChanges()
        }
    }


    private fun saveChanges() {
        viewModel.updateTrack(
            track_title.text.toString(),
            track_album.text.toString(),
            track_artist.text.toString(),
            track_position.text.toString().toInt(),
            track_genre.text.toString()
        )
    }

}