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
import com.omplayer.app.viewmodels.AlbumViewModel
import kotlinx.android.synthetic.main.fragment_album.*


open class AlbumFragment : Fragment(), View.OnClickListener {

    var albums = LibraryUtil.albums

    private val viewModel: AlbumViewModel by lazy {
        ViewModelProviders.of(this).get(AlbumViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

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

        filter_list.setOnClickListener(this)
        restore_list.setOnClickListener(this)

        if (albums.isNotEmpty()) {
            val albumList = activity!!.findViewById<RecyclerView>(R.id.album_list_recycler_view)
            albumList.layoutManager = layoutManager
            albumList.adapter = viewModel.itemAdapter
        }

        viewModel.viewLiveData.observe(this, Observer {
            it.findNavController().navigate(R.id.action_libraryFragment_to_singleAlbumFragment)
        })
    }

    override fun onClick(v: View?) {
        when (v) {
            filter_list -> {
                if (filter_between.text.toString().isNotEmpty() && filter_and.text.toString().isNotEmpty()) {
                    viewModel.filterAlbumsByYear(filter_between.text.toString(), filter_and.text.toString())
                    album_list_recycler_view.adapter = viewModel.itemAdapter
                }
            }
            restore_list -> {
                filter_between.setText("")
                filter_and.setText("")
                viewModel.restoreAlbums()
                album_list_recycler_view.adapter = viewModel.itemAdapter
            }
        }
    }
}