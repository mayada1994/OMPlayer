package com.example.android.omplayer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.omplayer.R
import com.example.android.omplayer.activities.MainActivity
import com.example.android.omplayer.adapters.SingleGenreAdapter
import com.example.android.omplayer.di.SingletonHolder
import com.example.android.omplayer.utils.LibraryUtil
import com.example.android.omplayer.viewmodels.GenreViewModel
import kotlinx.android.synthetic.main.fragment_single_genre.*

class SingleGenreFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity)
            .setActionBarTitle("Genres")
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        return inflater.inflate(R.layout.fragment_single_genre, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = GenreViewModel(SingletonHolder.application)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL

        val tracks = LibraryUtil.selectedGenreTracklist

        if (tracks.isNotEmpty()) {
            val itemAdapter = SingleGenreAdapter(tracks, this@SingleGenreFragment)
            single_genre_name.text = viewModel.getGenreName()
            val trackList = activity!!.findViewById<RecyclerView>(R.id.single_genre_list_recycler_view)
            trackList.layoutManager = layoutManager
            trackList.adapter = itemAdapter
        }
    }

    fun openPlayer() {
        val activity = activity as MainActivity
        activity.openPlayerFragment()
    }
}
