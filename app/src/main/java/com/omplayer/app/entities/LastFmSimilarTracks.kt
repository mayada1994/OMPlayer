package com.omplayer.app.entities

import com.google.gson.annotations.SerializedName

data class LastFmSimilarTracks(
    @SerializedName("track") val similarTracksList: ArrayList<LastFmSimilarTrack>
)