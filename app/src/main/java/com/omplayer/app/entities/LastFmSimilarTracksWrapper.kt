package com.omplayer.app.entities

import com.google.gson.annotations.SerializedName

data class LastFmSimilarTracksWrapper(
    @SerializedName("similartracks") val similarTracks: LastFmSimilarTracks
)