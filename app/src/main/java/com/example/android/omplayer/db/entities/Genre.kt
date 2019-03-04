package com.example.android.omplayer.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "genres")
data class Genre(
    var name: String = ""
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}