package com.example.android.omplayer.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.omplayer.R
import com.example.android.omplayer.adapters.ArtistAdapter
import com.example.android.omplayer.db.entities.Artist
import com.example.android.omplayer.viewmodels.ArtistViewModel


var artists = ArrayList<Artist>()

class ArtistFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_artist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val artistViewModel = ArtistViewModel(context!!)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL

        try {
            artists = artistViewModel.getArtistsFromDb().value as ArrayList<Artist>
        } catch (e: Exception) {

        }

        if (artists.isNotEmpty()) {
            val itemAdapter = ArtistAdapter(artists)

            val artistList = activity!!.findViewById<RecyclerView>(R.id.artist_list_recycler_view)
            artistList.layoutManager = layoutManager
            artistList.adapter = itemAdapter
        }
    }
}