package com.omplayer.app.entities

import com.google.gson.annotations.SerializedName

data class LastFmSimilarArtist(
    @SerializedName("name") val name: String,
    @SerializedName("match") val match: Float,
    @SerializedName("image") val images: ArrayList<LastFmImage>
)