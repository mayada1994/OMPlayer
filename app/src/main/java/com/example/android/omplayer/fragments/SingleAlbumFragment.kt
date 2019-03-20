package com.example.android.omplayer.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.android.omplayer.R
import com.example.android.omplayer.activities.MainActivity
import com.example.android.omplayer.adapters.SingleAlbumAdapter
import com.example.android.omplayer.di.SingletonHolder
import com.example.android.omplayer.utils.LibraryUtil
import com.example.android.omplayer.viewmodels.AlbumViewModel
import kotlinx.android.synthetic.main.fragment_single_album.*
import java.io.File

class SingleAlbumFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity)
            .setActionBarTitle("Albums")
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        return inflater.inflate(R.layout.fragment_single_album, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = AlbumViewModel(SingletonHolder.application)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL

        val tracks = LibraryUtil.selectedAlbumTracklist

        if (tracks.isNotEmpty()) {
            val itemAdapter = SingleAlbumAdapter(tracks, this@SingleAlbumFragment)
            single_album_name.text = viewModel.getAlbumName()
            viewModel.getAlbumArtist(single_album_artist)
            single_album_year.text = viewModel.getAlbumYear()
            loadImage(viewModel.getAlbumCoverUrl())
            val trackList = activity!!.findViewById<RecyclerView>(R.id.single_album_recycler_list)
            trackList.layoutManager = layoutManager
            trackList.adapter = itemAdapter
        }
    }

    fun loadImage(albumArtUrl: String) {
        val file = File(albumArtUrl)
        val uri = Uri.fromFile(file)

        Glide.with(context!!).load(uri)
            .apply(RequestOptions().placeholder(R.drawable.placeholder).error(R.drawable.placeholder))
            .into(single_album_img)
    }

    fun openPlayer() {
        val activity = activity as MainActivity
        activity.openPlayerFragment()
    }
}
