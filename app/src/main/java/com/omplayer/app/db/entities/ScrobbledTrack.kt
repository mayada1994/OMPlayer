package com.omplayer.app.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scrobbled")
data class ScrobbledTrack(
    @ColumnInfo(name = "album_title")
    var albumTitle: String = "",
    @ColumnInfo(name = "artist_title")
    var artistTitle: String = "",
    @ColumnInfo(name = "title")
    var title: String = "",
    @ColumnInfo(name = "timestamp")
    var timestamp: String = ""
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0
}