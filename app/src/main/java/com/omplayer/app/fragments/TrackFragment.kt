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
import com.omplayer.app.viewmodels.TrackViewModel

class TrackFragment : Fragment() {

    var tracks = LibraryUtil.tracks

    private val viewModel: TrackViewModel by lazy {
        ViewModelProviders.of(this).get(TrackViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_track, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL

        if (tracks.isNotEmpty()) {
            val trackList = activity!!.findViewById<RecyclerView>(R.id.track_list_recycler_view)
            trackList.layoutManager = layoutManager
            trackList.adapter = viewModel.itemAdapter
        }

        viewModel.viewLiveData.observe(this, Observer {
            it.findNavController().navigate(R.id.action_libraryFragment_to_playerFragment)
        } )
    }

//    fun openPlayer() {
//        val activity = activity as MainActivity
//        activity.openPlayerFragment()
//    }
}