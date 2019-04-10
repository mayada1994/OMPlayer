package com.omplayer.app.entities

import com.google.gson.annotations.SerializedName

data class LastFmImage(
    @SerializedName("size") val size: String,
    @SerializedName("#text") val url: String
)