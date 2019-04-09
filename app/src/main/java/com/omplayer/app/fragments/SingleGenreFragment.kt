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
import com.omplayer.app.adapters.SingleGenreAdapter
import com.omplayer.app.di.SingletonHolder
import com.omplayer.app.utils.LibraryUtil
import com.omplayer.app.viewmodels.GenreViewModel
import com.omplayer.app.viewmodels.SingleGenreViewModel
import com.omplayer.app.viewmodels.TrackViewModel
import kotlinx.android.synthetic.main.fragment_single_genre.*

class SingleGenreFragment : Fragment() {

    private val viewModel: SingleGenreViewModel by lazy {
        ViewModelProviders.of(this).get(SingleGenreViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity)
            .setActionBarTitle(getString(R.string.genres))
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        return inflater.inflate(R.layout.fragment_single_genre, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL

        viewModel.viewLiveData.observe(this, Observer {
            it.findNavController().navigate(R.id.action_singleGenreFragment_to_playerFragment)
        } )


        val tracks = LibraryUtil.selectedGenreTracklist

        if (tracks.isNotEmpty()) {
            single_genre_name.text = viewModel.getGenreName()
            val trackList = activity!!.findViewById<RecyclerView>(R.id.single_genre_list_recycler_view)
            trackList.layoutManager = layoutManager
            trackList.adapter = viewModel.itemAdapter
        }


    }

}
