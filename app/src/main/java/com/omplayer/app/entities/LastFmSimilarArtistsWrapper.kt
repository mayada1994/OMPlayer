package com.omplayer.app.entities

import com.google.gson.annotations.SerializedName

data class LastFmSimilarArtistsWrapper(
    @SerializedName("similarartists") val user: LastFmSimilarArtists
)