package com.omplayer.app.entities

import com.google.gson.annotations.SerializedName

data class LastFmTrack(
    @SerializedName("album") val album: LastFmAlbum
)