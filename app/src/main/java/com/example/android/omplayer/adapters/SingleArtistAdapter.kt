package com.example.android.omplayer.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.android.omplayer.R
import com.example.android.omplayer.db.entities.Album
import com.example.android.omplayer.fragments.SingleArtistFragment
import com.example.android.omplayer.utils.LibraryUtil
import com.mikhaellopez.circularimageview.CircularImageView
import java.io.File

class SingleArtistAdapter(val albums: List<Album>, val fragment: SingleArtistFragment) :
    RecyclerView.Adapter<SingleArtistAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_single_album, viewGroup, false)
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

        var itemName: TextView = itemView.findViewById(R.id.single_album_title)
        var itemYear: TextView = itemView.findViewById(R.id.single_album_year)
        var itemCover: CircularImageView = itemView.findViewById(R.id.single_album_cover_img)

        init {
            itemView.setOnClickListener {
                LibraryUtil.selectedArtist = position
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