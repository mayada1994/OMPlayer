package com.example.android.omplayer.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "albums",
    foreignKeys = [ForeignKey(
        entity = Artist::class,
        parentColumns = ["id"], childColumns = ["artistId"]
    ), ForeignKey(
        entity = Genre::class,
        parentColumns = ["id"], childColumns = ["genreId"]
    )]
)
data class Album(
    var title: String = "",
    var cover: String = "",
    var year: Int = 0,
    @ColumnInfo(name = "artist_id")
    var artistId: Int = 0,
    @ColumnInfo(name = "genre_id")
    var genreId: Int = 0
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}