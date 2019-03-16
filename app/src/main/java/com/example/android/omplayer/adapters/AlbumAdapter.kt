package com.example.android.omplayer.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.omplayer.R
import com.example.android.omplayer.db.entities.Album
import com.bumptech.glide.Glide
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.request.RequestOptions
import java.io.File


class AlbumAdapter(val albums: List<Album>) : RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_album, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemName.text = albums[position].title
        viewHolder.itemYear.text = albums[position].year
        viewHolder.loadImage(albums[position].cover)
    }

    override fun getItemCount(): Int {
        return albums.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var itemName: TextView = itemView.findViewById(R.id.album_title)
        var itemYear: TextView = itemView.findViewById(R.id.album_year)
        var itemCover: ImageView = itemView.findViewById(R.id.img_cover)

        init {
            itemView.setOnClickListener {
                Log.i("TAG!!!", albums[position].toString())
            }
        }

        fun loadImage(albumArtUrl: String) {
            val file = File(albumArtUrl)
            val uri = Uri.fromFile(file)

            Glide.with(itemView).load(uri)
                .apply(RequestOptions().placeholder(R.drawable.placeholder).error(R.drawable.placeholder))
                .into(itemCover)

        }
    }
}