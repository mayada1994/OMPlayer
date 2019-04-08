package com.omplayer.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.omplayer.app.R
import com.omplayer.app.utils.LibraryUtil
import com.omplayer.app.viewmodels.ArtistViewModel


class ArtistFragment : Fragment() {

    var artists = LibraryUtil.artists

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_artist, container, false)
    }

    private val viewModel: ArtistViewModel by lazy {
        ViewModelProviders.of(this).get(ArtistViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL

        if (artists.isNotEmpty()) {
            val artistList = activity!!.findViewById<RecyclerView>(R.id.artist_list_recycler_view)
            artistList.layoutManager = layoutManager
            artistList.adapter = viewModel.itemAdapter
        }

        viewModel.viewLiveData.observe(this, Observer {
            it.findNavController().navigate(R.id.action_libraryFragment_to_singleArtistFragment)
        })

    }

//    fun selectArtist() {
//        val activity = activity as MainActivity
//        activity.selectArtist()
//    }
}