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
import com.example.android.omplayer.adapters.ArtistAdapter
import com.example.android.omplayer.utils.LibraryUtil


class ArtistFragment : Fragment() {

    var artists = LibraryUtil.artists

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_artist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL

        if (artists.isNotEmpty()) {
            val itemAdapter = ArtistAdapter(artists, this@ArtistFragment)

            val artistList = activity!!.findViewById<RecyclerView>(R.id.artist_list_recycler_view)
            artistList.layoutManager = layoutManager
            artistList.adapter = itemAdapter
        }
    }

    fun selectArtist() {
        val activity = activity as MainActivity
        activity.selectArtist()
    }
}