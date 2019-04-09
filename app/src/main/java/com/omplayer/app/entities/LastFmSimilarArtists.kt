package com.omplayer.app.entities

import com.google.gson.annotations.SerializedName

data class LastFmSimilarArtists(
    @SerializedName("artist") val similarArtists: ArrayList<LastFmSimilarArtist>
)