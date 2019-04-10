package com.omplayer.app.repositories

import androidx.annotation.WorkerThread
import com.omplayer.app.db.entities.Track
import com.omplayer.app.di.SingletonHolder.db

class EditTagRepository {

    @WorkerThread
    fun getGenreId(name: String): Int {
        return db.genreDao().getGenreByName(name).id
    }

    @WorkerThread
    fun getArtistId(name: String): Int {
        return db.artistDao().getArtistByName(name).id
    }

    @WorkerThread
    fun getAlbumId(title: String, artist: String, year: String): Int {
        val artistId = getArtistId(artist)
        return try {
            db.albumDao().getAlbumByTrack(artistId, title, year).id
        } catch (e: Exception) {
            db.albumDao().getAlbumByTitle(title).id
        }
    }

    @WorkerThread
    fun getTrackAlbum(currentTrack: Track): String {
        return db.albumDao().getAlbumById(currentTrack.albumId).title
    }

    @WorkerThread
    fun getTrackArtist(currentTrack: Track): String {
        return db.artistDao().getArtistById(currentTrack.artistId).name
    }

    @WorkerThread
    fun getTrackGenre(currentTrack: Track): String {
        return db.genreDao().getGenreById(currentTrack.genreId).name
    }

    @WorkerThread
    fun getTrackAlbumCover(currentTrack: Track): String {
        return db.albumDao().getAlbumById(currentTrack.albumId).cover
    }
}