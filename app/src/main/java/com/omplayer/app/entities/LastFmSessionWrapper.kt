package com.omplayer.app.entities

import com.google.gson.annotations.SerializedName

data class LastFmSessionWrapper(
    @SerializedName("session") val session: LastFmSession
)
