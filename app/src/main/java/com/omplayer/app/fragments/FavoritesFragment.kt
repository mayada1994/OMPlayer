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
import com.omplayer.app.activities.MainActivity
import com.omplayer.app.db.entities.Track
import com.omplayer.app.utils.LibraryUtil
import com.omplayer.app.viewmodels.FavoritesViewModel
import kotlinx.android.synthetic.main.fragment_favorites.*

class FavoritesFragment : Fragment() {

    private val viewModel: FavoritesViewModel by lazy {
        ViewModelProviders.of(this).get(FavoritesViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    var tracks: List<Track> = LibraryUtil.favorites


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity)
            .supportActionBar?.show()
        (activity as MainActivity)
            .setActionBarTitle(getString(R.string.favorites))
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(viewModel)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL

        viewModel.viewLiveData.observe(this, Observer {
            it.findNavController().navigate(R.id.action_favoritesFragment_to_playerFragment)
        })

        if (tracks.isNotEmpty()) {
            val trackList = activity!!.findViewById<RecyclerView>(R.id.track_list_recycler_view)
            trackList.layoutManager = layoutManager
            trackList.adapter = viewModel.itemAdapter
        } else {
            track_list_recycler_view.visibility = View.GONE
            message_no_favorites.visibility = View.VISIBLE

        }

    }

}