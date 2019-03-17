package com.example.android.omplayer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.omplayer.R
import com.example.android.omplayer.activities.MainActivity
import com.example.android.omplayer.adapters.TrackAdapter
import com.example.android.omplayer.utils.LibraryUtil

class TrackFragment : Fragment() {


    var tracks = LibraryUtil.tracks

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_track, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL

        if (tracks.isNotEmpty()) {
            val itemAdapter = TrackAdapter(tracks, this@TrackFragment)

            val trackList = activity!!.findViewById<RecyclerView>(R.id.track_list_recycler_view)
            trackList.layoutManager = layoutManager
            trackList.adapter = itemAdapter
        }
    }

    fun openPlayer() {
        val activity = activity as MainActivity
        activity.openPlayerFragment()
    }
}