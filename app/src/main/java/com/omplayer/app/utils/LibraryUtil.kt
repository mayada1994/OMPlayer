package com.omplayer.app.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.omplayer.app.db.entities.Album
import com.omplayer.app.db.entities.Artist
import com.omplayer.app.db.entities.Genre
import com.omplayer.app.db.entities.Track
import com.omplayer.app.stateMachine.Action

object LibraryUtil {
    var selectedGenre = 0
    var selectedArtist = 0
    var selectedAlbum = 0
    var selectedTrack = 0
    var selectedTrackVideoId = ""

    var selectedGenreTracklist: List<Track> = emptyList()
    var selectedArtistAlbumList: List<Album> = emptyList()
    var selectedAlbumTracklist: List<Track> = emptyList()
    var currentAlbumList: List<Album> = emptyList()
    var tracklist: List<Track> = emptyList()
    var genres: List<Genre> = emptyList()
    var artists: List<Artist> = emptyList()
    var albums: List<Album> = emptyList()
    var tracks: List<Track> = emptyList()
    var favorites: List<Track> = emptyList()
    var action: Action = Action.Pause()
    var liveData : MutableLiveData<Track> = MutableLiveData()
}