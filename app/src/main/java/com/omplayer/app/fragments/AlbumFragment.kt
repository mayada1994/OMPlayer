package com.omplayer.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.omplayer.app.R
import com.omplayer.app.activities.MainActivity
import com.omplayer.app.adapters.AlbumAdapter
import com.omplayer.app.utils.LibraryUtil


open class AlbumFragment : Fragment(), BaseAlbumFragment {

    var albums = LibraryUtil.albums

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_album, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL
        val itemAdapter = AlbumAdapter(albums, this@AlbumFragment)

        if (albums.isNotEmpty()) {
            val albumList = activity!!.findViewById<RecyclerView>(R.id.album_list_recycler_view)
            albumList.layoutManager = layoutManager
            albumList.adapter = itemAdapter
        }
    }

    override fun selectAlbum() {
        val activity = activity as MainActivity
        activity.selectAlbum()
    }
}