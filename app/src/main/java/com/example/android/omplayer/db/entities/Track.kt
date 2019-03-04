package com.example.android.omplayer.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "tracks",
    foreignKeys = [ForeignKey(
        entity = Album::class,
        parentColumns = ["id"], childColumns = ["albumId"]
    )]
)
data class Track(
    var title: String = "",
    var position: String = "",
    var duration: Int = 0,
    @ColumnInfo(name = "album_id")
    var albumId: Int = 0,
    var path: String = ""
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}