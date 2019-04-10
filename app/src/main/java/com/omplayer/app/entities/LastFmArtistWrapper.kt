package com.omplayer.app.entities

import com.google.gson.annotations.SerializedName

data class LastFmArtistWrapper(
    @SerializedName("artist") val artist: LastFmArtist

)