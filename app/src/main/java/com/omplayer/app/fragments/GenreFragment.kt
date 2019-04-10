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
import com.omplayer.app.utils.LibraryUtil
import com.omplayer.app.viewmodels.GenreViewModel

class GenreFragment : Fragment() {


    var genres = LibraryUtil.genres

    private val viewModel: GenreViewModel by lazy {
        ViewModelProviders.of(this).get(GenreViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        return inflater.inflate(R.layout.fragment_genre, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL

        if (genres.isNotEmpty()) {

            val genreList = activity!!.findViewById<RecyclerView>(R.id.genre_list_recycler_view)
            genreList.layoutManager = layoutManager
            genreList.adapter = viewModel.genreAdapter
        }

        viewModel.viewLiveData.observe(this, Observer {
            it.findNavController().navigate(R.id.action_libraryFragment_to_singleGenreFragment)
        })


    }

//    fun selectGenre() {
//        val activity = activity as MainActivity
//        activity.selectGenre()
//    }
}