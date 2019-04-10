package com.omplayer.app.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.omplayer.app.R
import com.omplayer.app.db.entities.Artist
import com.omplayer.app.db.entities.Track
import com.omplayer.app.stateMachine.Action
import com.omplayer.app.utils.LibraryUtil

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

    companion object {
        const val TAG = "TrackAdapter"
    }

    interface Callback {
        fun openPlayer(view: View)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_track, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val (track, artist) = items[position]
        viewHolder.trackName.text = track.title
        viewHolder.trackArtist.text = artist.name

        viewHolder.path = track.path
    }


    override fun getItemCount(): Int = items.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        var trackName: TextView = itemView.findViewById(R.id.track_title)
        var trackArtist: TextView = itemView.findViewById(R.id.track_artist)
        lateinit var path: String

        init {
            itemView.setOnClickListener {
                LibraryUtil.tracklist = LibraryUtil.tracks
                LibraryUtil.selectedTrack = position
                LibraryUtil.action = Action.Play()
                Log.d(TAG,LibraryUtil.selectedTrack.toString())
                callback.openPlayer(it)

            }
        }
    }
}