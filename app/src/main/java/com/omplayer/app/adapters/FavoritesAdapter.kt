package com.omplayer.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.omplayer.app.R
import com.omplayer.app.stateMachine.Action
import com.omplayer.app.utils.LibraryUtil
import com.omplayer.app.utils.LibraryUtil.favorites


class FavoritesAdapter(val callback: Callback) :
    RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {


    interface Callback {
        fun openPlayer(view: View)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): FavoritesAdapter.ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_track, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemName.text = favorites[position].title
        viewHolder.path = favorites[position].path
    }


    override fun getItemCount(): Int {
        return favorites.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        var itemName: TextView = itemView.findViewById(R.id.track_title)
        lateinit var path: String

        init {
            itemView.setOnClickListener {
                LibraryUtil.tracklist = LibraryUtil.favorites
                LibraryUtil.selectedTrack = position
                LibraryUtil.action = Action.Play()
                callback.openPlayer(it)
            }
        }
    }
}