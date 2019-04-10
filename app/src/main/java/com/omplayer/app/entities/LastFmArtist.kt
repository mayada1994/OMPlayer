package com.omplayer.app.entities

import com.google.gson.annotations.SerializedName

data class LastFmArtist(
    @SerializedName("image") val images: ArrayList<LastFmImage>
)