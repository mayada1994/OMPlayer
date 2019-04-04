package com.omplayer.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.omplayer.app.R
import com.omplayer.app.activities.MainActivity
import com.omplayer.app.adapters.FavoritesAdapter
import com.omplayer.app.db.entities.Track
import com.omplayer.app.utils.LibraryUtil

class FavoritesFragment : Fragment() {

    var tracks: ArrayList<Track> = LibraryUtil.favorites


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL

        if (tracks.isNotEmpty()) {
            val itemAdapter = FavoritesAdapter(tracks, this@FavoritesFragment)

            val trackList = activity!!.findViewById<RecyclerView>(R.id.track_list_recycler_view)
            trackList.layoutManager = layoutManager
            trackList.adapter = itemAdapter
        }
    }


    fun openPlayer(view: View) {
//        val activity = activity as MainActivity
////        activity.openPlayerFragment()
        view.findNavController().navigate(R.id.action_favoritesFragment_to_playerFragment)
    }
}