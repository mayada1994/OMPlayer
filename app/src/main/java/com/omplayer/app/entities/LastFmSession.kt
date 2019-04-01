package com.omplayer.app.entities

import com.google.gson.annotations.SerializedName

data class LastFmSession(
    @SerializedName("subscriber") val subscriber: String,
    @SerializedName("name") val name: String,
    @SerializedName("key") val key: String
)