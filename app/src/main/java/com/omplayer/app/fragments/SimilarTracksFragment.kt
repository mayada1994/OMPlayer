package com.omplayer.app.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.omplayer.app.R
import com.omplayer.app.adapters.SimilarTrackAdapter
import com.omplayer.app.utils.LastFmUtil
import kotlinx.android.synthetic.main.fragment_similar_tracks.*

class SimilarTracksFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_similar_tracks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val layoutManager = GridLayoutManager(context, 2)
        layoutManager.orientation = RecyclerView.VERTICAL

        val similarTracks = LastFmUtil.similarTracks
        if (similarTracks.isNotEmpty()) {
            val itemAdapter = SimilarTrackAdapter(similarTracks, fragmentManager!!)
            original_song.text = getString(R.string.songs_similar_to_original, LastFmUtil.originalTrack)
            val similarTracksList = activity!!.findViewById<RecyclerView>(R.id.similar_tracks_list_grid_view)
            similarTracksList.layoutManager = layoutManager
            similarTracksList.adapter = itemAdapter
        }
    }
}