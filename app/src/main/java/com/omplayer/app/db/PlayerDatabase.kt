package com.omplayer.app.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.omplayer.app.db.dao.*
import com.omplayer.app.db.entities.*

@Database(entities = [Genre::class, Artist::class, Album::class, Track::class, ScrobbledTrack::class], version = 1)
abstract class PlayerDatabase : RoomDatabase() {

    abstract fun genreDao(): GenreDao
    abstract fun artistDao(): ArtistDao
    abstract fun albumDao(): AlbumDao
    abstract fun trackDao(): TrackDao
    abstract fun scrobbledTrackDao(): ScrobbledTrackDao

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
                    "omplayer.db"
                ).build().also { INSTANCE = it }
                INSTANCE = instance
                return instance
            }
        }
    }
}