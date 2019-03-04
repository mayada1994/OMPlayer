package com.example.android.omplayer.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "artists")
data class Artist(
    var name: String = "",
    var image: String = ""
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}