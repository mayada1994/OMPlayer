package com.omplayer.app.entities

import com.google.gson.annotations.SerializedName

data class LastFmTrackWrapper(
    @SerializedName("track") val track: LastFmTrack
)