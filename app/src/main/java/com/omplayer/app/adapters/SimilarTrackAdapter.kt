package com.omplayer.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mikhaellopez.circularimageview.CircularImageView
import com.omplayer.app.R
import com.omplayer.app.di.SingletonHolder
import com.omplayer.app.entities.LastFmImage
import com.omplayer.app.entities.LastFmSimilarTrack
import com.omplayer.app.viewmodels.VideoViewModel

class SimilarTrackAdapter(val similarTracks: List<LastFmSimilarTrack>, val fragmentManager: FragmentManager) :
    RecyclerView.Adapter<SimilarTrackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_similar_track, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemName.text = similarTracks[position].name
        viewHolder.itemArtist.text = similarTracks[position].artist.name
        viewHolder.loadImage(similarTracks[position].images)
    }

    override fun getItemCount(): Int {
        return similarTracks.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var itemName: TextView = itemView.findViewById(R.id.similar_track_title)
        var itemArtist: TextView = itemView.findViewById(R.id.similar_track_artist)
        var itemCover: CircularImageView = itemView.findViewById(R.id.similar_track_cover_img)

        init {
            itemView.setOnClickListener {
                val videoViewModel = VideoViewModel(SingletonHolder.application)
                videoViewModel.getVideoId(similarTracks[position].url, true, fragmentManager)

            }
        }

        fun loadImage(images: List<LastFmImage>) {
            images.forEach {
                if (it.size == "extralarge") {
                    val imageUrl = it.url

                    Glide.with(SingletonHolder.application).load(imageUrl)
                        .apply(RequestOptions().placeholder(R.drawable.ic_last_fm_placeholder).error(R.drawable.ic_last_fm_placeholder))
                        .into(itemCover)
                }
            }
        }
    }
}