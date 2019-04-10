package com.omplayer.app.entities

import com.google.gson.annotations.SerializedName

data class LastFmUserWrapper(
    @SerializedName("user") val user: LastFmUser
)