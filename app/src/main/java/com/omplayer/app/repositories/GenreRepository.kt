package com.omplayer.app.repositories

import androidx.annotation.WorkerThread
import com.omplayer.app.db.dao.GenreDao
import com.omplayer.app.db.entities.Genre

class GenreRepository(private val genreDao: GenreDao) {

    @WorkerThread
    suspend fun insertGenre(genre: Genre) {
        genreDao.insert(genre)
    }

    @WorkerThread
    suspend fun insertAllGenres(genre: ArrayList<Genre>) {
        genreDao.insertAll(*genre.toTypedArray())
    }

    @WorkerThread
    suspend fun updateGenre(genre: Genre) {
        genreDao.update(genre)
    }

    @WorkerThread
    suspend fun deleteGenre(genre: Genre) {
        genreDao.delete(genre)
    }

    @WorkerThread
    suspend fun deleteAllGenres() {
        try {
            genreDao.deleteAll()
        } catch (e: Exception) {
        }
    }

    @WorkerThread
    suspend fun getAllGenres(): List<Genre>? {
        return genreDao.getAllGenres()
    }

    @WorkerThread
    suspend fun getAllNotEmptyGenres(): List<Genre>? {
        return genreDao.getGenresWithSongs()
    }

    @WorkerThread
    suspend fun getGenreById(id: Int): Genre? {
        return genreDao.getGenreById(id)
    }

    @WorkerThread
    suspend fun getGenreByName(name: String): Genre? {
        return genreDao.getGenreByName(name)
    }
}