package com.omplayer.app.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "tracks",
    foreignKeys = [ForeignKey(
        entity = Album::class,
        parentColumns = ["id"], childColumns = ["album_id"]
    ), ForeignKey(
        entity = Artist::class,
        parentColumns = ["id"], childColumns = ["artist_id"]
    ), ForeignKey(
        entity = Genre::class,
        parentColumns = ["id"], childColumns = ["genre_id"]
    )]
)
data class Track(
    @ColumnInfo(name = "title")
    var title: String = "",
    @ColumnInfo(name = "position")
    var position: Int = 0,
    @ColumnInfo(name = "duration")
    var duration: Int = 0,
    @ColumnInfo(name = "album_id")
    var albumId: Int = 0,
    @ColumnInfo(name = "artist_id")
    var artistId: Int = 0,
    @ColumnInfo(name = "genre_id")
    var genreId: Int = 0,
    @ColumnInfo(name = "path")
    var path: String = ""
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0
}