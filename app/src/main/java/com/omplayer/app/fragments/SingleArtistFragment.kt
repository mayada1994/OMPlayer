package com.omplayer.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.omplayer.app.R
import com.omplayer.app.activities.MainActivity
import com.omplayer.app.di.SingletonHolder
import com.omplayer.app.utils.LibraryUtil
import com.omplayer.app.viewmodels.ArtistViewModel
import com.omplayer.app.viewmodels.LastFmViewModel
import com.omplayer.app.viewmodels.SingleArtistViewModel
import kotlinx.android.synthetic.main.fragment_single_artist.*

class SingleArtistFragment : Fragment() {

    private val singleArtistViewModel: SingleArtistViewModel by lazy {
        ViewModelProviders.of(this).get(SingleArtistViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity)
            .setActionBarTitle(getString(R.string.artists))
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        return inflater.inflate(R.layout.fragment_single_artist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ArtistViewModel(SingletonHolder.application)
        val lastFmViewModel = LastFmViewModel(SingletonHolder.application)
        val layoutManager = GridLayoutManager(context, 2)
        layoutManager.orientation = RecyclerView.VERTICAL

        singleArtistViewModel.viewLiveData.observe(this, Observer {
            it.findNavController().navigate(R.id.action_singleArtistFragment_to_singleAlbumFragment)
        })

        if(viewModel.getArtist().image.isEmpty()) {
            lastFmViewModel.getArtistInfo(viewModel.getArtist(), single_artist_img)
        }else{
            viewModel.loadImage(single_artist_img)
        }

        val albums = LibraryUtil.selectedArtistAlbumList

        if (albums.isNotEmpty()) {
            single_artist_name.text = viewModel.getArtist().name
            val albumList = activity!!.findViewById<RecyclerView>(R.id.single_artist_list_grid_view)
            albumList.layoutManager = layoutManager
            albumList.adapter = singleArtistViewModel.itemAdapter
        }
    }

}
