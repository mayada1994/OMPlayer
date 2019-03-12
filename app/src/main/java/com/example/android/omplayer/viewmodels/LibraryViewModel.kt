package com.example.android.omplayer.viewmodels

import android.content.Context
import com.example.android.omplayer.db.PlayerDatabase
import com.example.android.omplayer.db.entities.Genre
import com.example.android.omplayer.entities.LibraryUtil
import com.example.android.omplayer.repositories.*

class LibraryViewModel(val context: Context) {

    private val libraryRepository: LibraryRepository = LibraryRepository(context)
    private val genreRepository: GenreRepository = GenreRepository(context)
    private val artistRepository: ArtistRepository = ArtistRepository(context)
    private val albumRepository: AlbumRepository = AlbumRepository(context)
    private val trackRepository: TrackRepository = TrackRepository(context)

    private fun loadDataToLibraryUtil() {
        libraryRepository.prepareDataForDb()
        PlayerDatabase.getDatabase(context).genreDao().insert(Genre("Poppy"))
    }

    fun loadDataToDb() {
        if (LibraryUtil.tracks.isEmpty()) {
            loadDataToLibraryUtil()

            for (genre in LibraryUtil.genres) {
                genreRepository.insertGenre(genre)
            }

            for (artist in LibraryUtil.artists) {
                artistRepository.insertArtist(artist)
            }

            for (album in LibraryUtil.albums) {
                albumRepository.insertAlbum(album)
            }

            for (track in LibraryUtil.tracks) {
                trackRepository.insertTrack(track)
            }
        }
    }
}