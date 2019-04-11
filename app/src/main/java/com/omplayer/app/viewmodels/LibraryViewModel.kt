package com.omplayer.app.viewmodels

import android.app.Application
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.AndroidViewModel
import com.omplayer.app.db.entities.Album
import com.omplayer.app.db.entities.Artist
import com.omplayer.app.db.entities.Genre
import com.omplayer.app.db.entities.Track
import com.omplayer.app.di.SingletonHolder
import com.omplayer.app.repositories.*
import com.omplayer.app.utils.LibraryUtil
import com.omplayer.app.utils.PreferenceUtil
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
                try {

                    clearTables()
                    loadGenres()
                    loadArtists()
                    loadAlbums()
                    loadTracks()
                    loadFavorites()
                    deleteAdditionalGenres()
                } catch (e: Exception) {

                }

                withContext(Dispatchers.Main) {
                    PreferenceUtil.updateLibrary = false
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
    }

    suspend fun loadArtists() {
        val artists = libraryRepository.scanDeviceForArtists()
        artistRepository.insertAllArtists(artists)
        LibraryUtil.artists = db.artistDao().getAllArtists() as ArrayList<Artist>
    }

    suspend fun loadAlbums() {
        val albums = libraryRepository.scanDeviceForAlbums()
        albumRepository.inserAllAlbums(albums)
        LibraryUtil.albums = db.albumDao().getAllAlbums() as ArrayList<Album>
    }

    suspend fun loadTracks() {
        val tracks = libraryRepository.scanDeviceForTracks()
        trackRepository.insertAllTracks(tracks)
        LibraryUtil.tracks = db.trackDao().getAllTracks() as ArrayList<Track>
        LibraryUtil.tracklist = LibraryUtil.tracks
    }

    suspend fun loadFavorites() {
        val tracks = libraryRepository.scanDeviceForTracks()
        trackRepository.getTracksByFavorite(true)
        LibraryUtil.favorites = db.trackDao().getTracksByFavorite(true) as ArrayList<Track>
    }

    suspend fun deleteAdditionalGenres() {
        LibraryUtil.genres = LibraryUtil.genres.filter {
            db.trackDao().getTracksByGenreId(it.id).isEmpty()
        }
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
            LibraryUtil.genres = genreRepository.getAllGenres() as ArrayList<Genre>
            LibraryUtil.artists = artistRepository.getAllArtists() as ArrayList<Artist>
            LibraryUtil.albums = albumRepository.getAllAlbums() as ArrayList<Album>
            LibraryUtil.tracks = trackRepository.getAllTracks() as ArrayList<Track>
            LibraryUtil.favorites = trackRepository.getTracksByFavorite(true) as ArrayList<Track>
            LibraryUtil.tracklist = LibraryUtil.tracks
            withContext(Dispatchers.Main) {
                try {
                    LibraryUtil.liveData.value = LibraryUtil.tracklist[LibraryUtil.selectedTrack]
                }catch (e: Exception){

                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}