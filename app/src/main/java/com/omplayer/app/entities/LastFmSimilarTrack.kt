package com.omplayer.app.entities

import com.google.gson.annotations.SerializedName

data class LastFmSimilarTrack(
    @SerializedName("name") val name: String,
    @SerializedName("match") val match: Float,
    @SerializedName("url") val url: String,
    @SerializedName("artist") val artist: LastFmSimilarTrackArtist,
    @SerializedName("image") val images: ArrayList<LastFmImage>
)