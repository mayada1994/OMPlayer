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
import com.omplayer.app.adapters.GenreAdapter
import com.omplayer.app.utils.LibraryUtil


class GenreFragment : Fragment() {


    var genres = LibraryUtil.genres

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
            val itemAdapter = GenreAdapter(genres, this@GenreFragment)

            val genreList = activity!!.findViewById<RecyclerView>(R.id.genre_list_recycler_view)
            genreList.layoutManager = layoutManager
            genreList.adapter = itemAdapter
        }
    }

    fun selectGenre() {
        val activity = activity as MainActivity
        activity.selectGenre()
    }
}