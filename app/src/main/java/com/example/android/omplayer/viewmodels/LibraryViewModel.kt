package com.example.android.omplayer.viewmodels

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.AndroidViewModel
import com.example.android.omplayer.db.entities.Album
import com.example.android.omplayer.db.entities.Artist
import com.example.android.omplayer.db.entities.Genre
import com.example.android.omplayer.db.entities.Track
import com.example.android.omplayer.di.SingletonHolder
import com.example.android.omplayer.repositories.*
import com.example.android.omplayer.utils.LibraryUtil
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class LibraryViewModel(application: Application) : AndroidViewModel(application) {

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.IO
    private val scope = CoroutineScope(coroutineContext)
    private val db = SingletonHolder.db

    private val libraryRepository: LibraryRepository = LibraryRepository(application.applicationContext)
    private val genreRepository: GenreRepository = GenreRepository(db.genreDao())
    private val artistRepository: ArtistRepository = ArtistRepository(db.artistDao())
    private val albumRepository: AlbumRepository = AlbumRepository(db.albumDao())
    private val trackRepository: TrackRepository = TrackRepository(db.trackDao())


    fun loadDataToDb(progressBar: ProgressBar) {
        scope.launch {
            withContext(coroutineContext) {

                clearTables()
                loadGenres()
                loadArtists()
                loadAlbums()
                loadTracks()

                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

    suspend fun clearTables() {
        //Do not change order
        trackRepository.deleteAllTracks()
        artistRepository.deleteAllArtists()
        albumRepository.deleteAllAlbums()
        genreRepository.deleteAllGenres()
    }

    suspend fun loadGenres() {
        val genres = libraryRepository.scanDeviceForGenres()
        genreRepository.insertAllGenres(genres)
        LibraryUtil.genres = db.genreDao().getAllGenres() as ArrayList<Genre>
        Log.d("TAG!!!", genres.size.toString())
    }

    suspend fun loadArtists() {
        val artists = libraryRepository.scanDeviceForArtists()
        artistRepository.insertAllArtists(artists)
        LibraryUtil.artists = db.artistDao().getAllArtists() as ArrayList<Artist>
        Log.d("TAG!!!", artists.size.toString())
    }

    suspend fun loadAlbums() {
        val albums = libraryRepository.scanDeviceForAlbums()
        albumRepository.inserAllAlbums(albums)
        LibraryUtil.albums = db.albumDao().getAllAlbums() as ArrayList<Album>
        Log.d("TAG!!!", albums.size.toString())
    }

    suspend fun loadTracks() {
        val tracks = libraryRepository.scanDeviceForTracks()
        trackRepository.insertAllTracks(tracks)
        LibraryUtil.tracks = db.trackDao().getAllTracks() as ArrayList<Track>
        LibraryUtil.tracklist = LibraryUtil.tracks
        Log.d("TAG!!!", tracks.size.toString())
    }

    fun emptyDb(): Boolean {
        var isEmptyDb: Boolean = false
        scope.launch {
            withContext(coroutineContext) {
                isEmptyDb = db.trackDao().getAllTracks().isEmpty()
            }
        }
        return isEmptyDb
    }

    fun extractData() {
        scope.launch {
            withContext(coroutineContext) {
                LibraryUtil.genres = db.genreDao().getAllGenres() as ArrayList<Genre>
                LibraryUtil.artists = db.artistDao().getAllArtists() as ArrayList<Artist>
                LibraryUtil.albums = db.albumDao().getAllAlbums() as ArrayList<Album>
                LibraryUtil.tracks = db.trackDao().getAllTracks() as ArrayList<Track>
                LibraryUtil.tracklist = LibraryUtil.tracks
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}