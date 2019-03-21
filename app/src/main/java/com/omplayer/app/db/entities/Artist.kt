package com.omplayer.app.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "artists")
data class Artist(
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "image")
    var image: String = ""
){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0
}