package com.omplayer.app.entities

import com.google.gson.annotations.SerializedName

data class LastFmSimilarTrackArtist(
    @SerializedName("name") val name: String
)