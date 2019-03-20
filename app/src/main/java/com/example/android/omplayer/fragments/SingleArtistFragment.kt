package com.example.android.omplayer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.omplayer.R
import com.example.android.omplayer.activities.MainActivity
import com.example.android.omplayer.adapters.SingleArtistAdapter
import com.example.android.omplayer.di.SingletonHolder
import com.example.android.omplayer.utils.LibraryUtil
import com.example.android.omplayer.viewmodels.ArtistViewModel
import kotlinx.android.synthetic.main.fragment_single_artist.*

class SingleArtistFragment : Fragment(), BaseAlbumFragment {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity)
            .setActionBarTitle("Artists")
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        return inflater.inflate(R.layout.fragment_single_artist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ArtistViewModel(SingletonHolder.application)
        val layoutManager = GridLayoutManager(context, 2)
        layoutManager.orientation = RecyclerView.VERTICAL

        val albums = LibraryUtil.selectedArtistAlbumList

        if (albums.isNotEmpty()) {
            val itemAdapter = SingleArtistAdapter(albums, this@SingleArtistFragment)
            single_artist_name.text = viewModel.getArtistName()
            val albumList = activity!!.findViewById<RecyclerView>(R.id.single_artist_list_grid_view)
            albumList.layoutManager = layoutManager
            albumList.adapter = itemAdapter
        }
    }

    override fun selectAlbum() {
        val activity = activity as MainActivity
        activity.selectAlbum()
    }
}
