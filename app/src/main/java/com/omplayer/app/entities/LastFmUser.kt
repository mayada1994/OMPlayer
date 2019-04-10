package com.omplayer.app.entities

import com.google.gson.annotations.SerializedName

data class LastFmUser(
    @SerializedName("image") val images: ArrayList<LastFmImage>
)