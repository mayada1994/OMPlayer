package com.omplayer.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.youtube.player.internal.c
import com.omplayer.app.R
import com.omplayer.app.db.entities.Artist
import com.omplayer.app.db.entities.Track
import com.omplayer.app.utils.LibraryUtil.tracks

class TrackAdapter(
    items: List<Item> = emptyList(),
    val callback: Callback
) : RecyclerView.Adapter<TrackAdapter.ViewHolder>() {

    var items: List<Item> = items
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    data class Item(
        val track: Track,
        val artist: Artist
    )


    interface Callback {
        fun openPlayer(position: Int, view: View)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_track, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val (track, artist) = items[position]
        viewHolder.trackName.text = track.title
        //If you need to see a position uncomment below
        //viewHolder.itemPosition.text = tracks[position].position.toString()
        viewHolder.trackArtist.text = artist.name

        viewHolder.path = track.path
    }


    override fun getItemCount(): Int = items.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        var trackName: TextView = itemView.findViewById(R.id.track_title)
        var trackArtist: TextView = itemView.findViewById(R.id.track_artist)
        var itemPosition: TextView = itemView.findViewById(R.id.track_position)
        lateinit var path: String

        init {
            itemView.setOnClickListener {

                callback.openPlayer(position, it)

            }
        }
    }
}