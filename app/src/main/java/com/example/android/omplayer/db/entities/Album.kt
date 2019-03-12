package com.example.android.omplayer.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "albums",
    foreignKeys = [ForeignKey(
        entity = Artist::class,
        parentColumns = ["id"], childColumns = ["artist_id"]
    )]
)
data class Album(
    @ColumnInfo(name = "title")
    var title: String = "",
    @ColumnInfo(name = "cover")
    var cover: String = "",
    @ColumnInfo(name = "year")
    var year: Int = 0,
    @ColumnInfo(name = "artist_id")
    var artistId: Int = 0
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0
}