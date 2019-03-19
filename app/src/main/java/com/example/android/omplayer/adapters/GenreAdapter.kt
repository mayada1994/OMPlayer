package com.example.android.omplayer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.omplayer.R
import com.example.android.omplayer.db.entities.Genre
import com.example.android.omplayer.di.SingletonHolder
import com.example.android.omplayer.fragments.GenreFragment
import com.example.android.omplayer.utils.LibraryUtil
import com.example.android.omplayer.viewmodels.GenreViewModel


class GenreAdapter(val genres: List<Genre>, val fragment:GenreFragment) : RecyclerView.Adapter<GenreAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_genre, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemName.text = genres[position].name
    }


    override fun getItemCount(): Int {
        return genres.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var itemName: TextView = itemView.findViewById(R.id.genre_name)

        init {
            itemView.setOnClickListener {
                val viewModel = GenreViewModel(SingletonHolder.application)
                LibraryUtil.selectedGenre = position
                viewModel.loadGenreTracks(LibraryUtil.genres[position].id, fragment)
            }
        }
    }
}