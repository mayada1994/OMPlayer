package com.example.android.omplayer.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.android.omplayer.R
import com.example.android.omplayer.adapters.GenreAdapter
import com.example.android.omplayer.utils.LibraryUtil


class GenreFragment : Fragment() {


    var genres = LibraryUtil.genres
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_genre, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL

        if (genres.isNotEmpty()) {
            val itemAdapter = GenreAdapter(genres)

            val genreList = activity!!.findViewById<RecyclerView>(R.id.genre_list_recycler_view)
            genreList.layoutManager = layoutManager
            genreList.adapter = itemAdapter
        }
    }
}