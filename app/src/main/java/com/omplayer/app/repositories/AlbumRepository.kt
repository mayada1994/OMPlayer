package com.omplayer.app.repositories

import androidx.annotation.WorkerThread
import com.omplayer.app.db.dao.AlbumDao
import com.omplayer.app.db.entities.Album

class AlbumRepository(private val albumDao: AlbumDao) {

    @WorkerThread
    suspend fun insertAlbum(album: Album) {
        albumDao.insert(album)
    }

    @WorkerThread
    suspend fun inserAllAlbums(album: ArrayList<Album>) {
        albumDao.insertAll(*album.toTypedArray())
    }

    @WorkerThread
    suspend fun updateAlbum(album: Album) {
        albumDao.update(album)
    }

    @WorkerThread
    suspend fun deleteAlbum(album: Album) {
        albumDao.delete(album)
    }

    @WorkerThread
    suspend fun deleteAllAlbums() {
        try {
            albumDao.deleteAll()
        } catch (e: Exception) {
        }
    }

    @WorkerThread
    suspend fun getAllAlbums(): List<Album>? {
        return albumDao.getAllAlbums()
    }

    @WorkerThread
    suspend fun getAlbumById(id: Int): Album? {
        return albumDao.getAlbumById(id)
    }

    @WorkerThread
    suspend fun getAlbumsByArtistId(id: Int): List<Album>? {
        return albumDao.getAlbumsByArtistId(id)
    }

    @WorkerThread
    suspend fun getAlbumByTrack(artistId: Int, albumTitle: String, albumYear: String): Album {
        return albumDao.getAlbumByTrack(artistId, albumTitle, albumYear)
    }
}