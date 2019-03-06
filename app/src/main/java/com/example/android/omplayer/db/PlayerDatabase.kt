package com.example.android.omplayer.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.android.omplayer.db.dao.AlbumDao
import com.example.android.omplayer.db.dao.ArtistDao
import com.example.android.omplayer.db.dao.GenreDao
import com.example.android.omplayer.db.dao.TrackDao
import com.example.android.omplayer.db.entities.Album
import com.example.android.omplayer.db.entities.Artist
import com.example.android.omplayer.db.entities.Genre
import com.example.android.omplayer.db.entities.Track

@Database(entities = [Genre::class, Artist::class, Album::class, Track::class], version = 1)
abstract class PlayerDatabase : RoomDatabase() {

    abstract fun genreDao(): GenreDao
    abstract fun artistDao(): ArtistDao
    abstract fun albumDao(): AlbumDao
    abstract fun trackDao(): TrackDao

    companion object {
        @Volatile
        private var INSTANCE: PlayerDatabase? = null

        fun getDatabase(context: Context): PlayerDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlayerDatabase::class.java,
                    "omplayer_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}