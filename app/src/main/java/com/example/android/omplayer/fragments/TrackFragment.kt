package com.example.android.omplayer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.omplayer.R
import com.example.android.omplayer.adapters.TrackAdapter
import com.example.android.omplayer.db.entities.Track
import com.example.android.omplayer.entities.LibraryUtil
import com.example.android.omplayer.viewmodels.TrackViewModel


var tracks = ArrayList<Track>()

class TrackFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_track, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val trackViewModel = TrackViewModel(context!!)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL

        try {
            tracks = trackViewModel.getTracksFromDb().value as ArrayList<Track>
        } catch (e: Exception) {
        }

        if (tracks.isNotEmpty()) {
            val itemAdapter = TrackAdapter(tracks)

            val trackList = activity!!.findViewById<RecyclerView>(R.id.track_list_recycler_view)
            trackList.layoutManager = layoutManager
            trackList.adapter = itemAdapter
        }
    }
}