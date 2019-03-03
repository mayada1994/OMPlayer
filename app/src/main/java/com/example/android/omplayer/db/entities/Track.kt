package com.example.android.omplayer.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Album::class,
        parentColumns = ["id"], childColumns = ["albumId"]
    )]
)
data class Track(
    @PrimaryKey(autoGenerate = true)
    val id: Int = -1,
    var title: String = "",
    val position: String = "",
    var duration: Int = -1,
    @ColumnInfo(name = "album_id")
    val albumId: Int = -1,
    var path: String = ""
)