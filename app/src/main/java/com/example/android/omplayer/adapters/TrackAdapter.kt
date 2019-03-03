package com.example.android.omplayer.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.omplayer.R
import com.example.android.omplayer.db.entities.Track
import com.example.android.omplayer.entities.LibraryUtil

class TrackAdapter(val tracks: List<Track>) : RecyclerView.Adapter<TrackAdapter.ViewHolder>() {

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
                Log.i("TAG!!!", path)
            }
        }
    }
}