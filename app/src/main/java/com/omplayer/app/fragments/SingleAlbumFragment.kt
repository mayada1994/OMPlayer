package com.omplayer.app.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.omplayer.app.R
import com.omplayer.app.activities.MainActivity
import com.omplayer.app.utils.LibraryUtil
import com.omplayer.app.viewmodels.SingleAlbumViewModel
import kotlinx.android.synthetic.main.fragment_single_album.*
import java.io.File

class SingleAlbumFragment : Fragment() {

    private val viewModel: SingleAlbumViewModel by lazy {
        ViewModelProviders.of(this).get(SingleAlbumViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity)
            .setActionBarTitle(getString(R.string.albums))
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        return inflater.inflate(R.layout.fragment_single_album, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL

        val tracks = LibraryUtil.selectedAlbumTracklist

        viewModel.viewLiveData.observe(this, Observer {
            it.findNavController().navigate(R.id.action_singleAlbumFragment_to_playerFragment)
        })

        if (tracks.isNotEmpty()) {
            single_album_name.text = viewModel.getAlbumName()
            viewModel.getAlbumArtist(single_album_artist)
            single_album_year.text = viewModel.getAlbumYear()
            loadImage(viewModel.getAlbumCoverUrl())
            val trackList = activity!!.findViewById<RecyclerView>(R.id.single_album_recycler_list)
            trackList.layoutManager = layoutManager
            trackList.adapter = viewModel.itemAdapter
        }
    }

    private fun loadImage(albumArtUrl: String) {
        val file = File(albumArtUrl)
        val uri = Uri.fromFile(file)

        Glide.with(context!!).load(uri)
            .apply(RequestOptions().placeholder(R.drawable.placeholder).error(R.drawable.placeholder))
            .into(single_album_img)
    }

}
