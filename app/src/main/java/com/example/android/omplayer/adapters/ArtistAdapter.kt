package com.example.android.omplayer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.omplayer.R
import com.example.android.omplayer.db.entities.Artist
import com.example.android.omplayer.di.SingletonHolder
import com.example.android.omplayer.fragments.ArtistFragment
import com.example.android.omplayer.utils.LibraryUtil
import com.example.android.omplayer.viewmodels.ArtistViewModel


class ArtistAdapter(val artists: List<Artist>, val fragment: ArtistFragment) : RecyclerView.Adapter<ArtistAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_artist, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemName.text = artists[position].name
    }


    override fun getItemCount(): Int {
        return artists.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var itemName: TextView = itemView.findViewById(R.id.artist_name)

        init {
            itemView.setOnClickListener {
                val viewModel = ArtistViewModel(SingletonHolder.application)
                LibraryUtil.selectedArtist = position
                viewModel.loadArtistAlbums(LibraryUtil.artists[position].id, fragment)
            }
        }
    }
}