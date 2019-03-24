package com.omplayer.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.omplayer.app.R
import com.omplayer.app.db.entities.Track
import com.omplayer.app.fragments.TrackFragment
import com.omplayer.app.stateMachine.Action
import com.omplayer.app.utils.LibraryUtil

class TrackAdapter(val tracks: List<Track>, val fragment: TrackFragment) :
    RecyclerView.Adapter<TrackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_track, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemName.text = tracks[position].title
        viewHolder.path = tracks[position].path
    }


    override fun getItemCount(): Int {
        return tracks.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var itemName: TextView = itemView.findViewById(R.id.track_title)
        lateinit var path: String

        init {
            itemView.setOnClickListener {
                LibraryUtil.tracklist = LibraryUtil.tracks
                LibraryUtil.selectedTrack = position
                LibraryUtil.action = Action.Play()
                fragment.openPlayer()
            }
        }
    }
}