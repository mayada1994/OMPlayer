package com.example.android.omplayer.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    foreignKeys = [ForeignKey(
        entity = Artist::class,
        parentColumns = ["id"], childColumns = ["artistId"]
    ), ForeignKey(
        entity = Genre::class,
        parentColumns = ["id"], childColumns = ["genreId"]
    )]
)
data class Album(
    @PrimaryKey(autoGenerate = true)
    val id: Int = -1,
    var title: String = "",
    val cover: String = "",
    var year: Int = -1,
    @ColumnInfo(name = "artist_id")
    val artistId: Int = -1,
    @ColumnInfo(name = "genre_id")
    val genreId: Int = -1
)