
package com.omplayer.app.utils

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

    var selectedGenreTracklist = ArrayList<Track>()
    var selectedArtistAlbumList = ArrayList<Album>()
    var selectedAlbumTracklist = ArrayList<Track>()
    var currentAlbumList = ArrayList<Album>()
    var tracklist = ArrayList<Track>()
    var genres = ArrayList<Genre>()
    var artists = ArrayList<Artist>()
    var albums = ArrayList<Album>()
    var tracks = ArrayList<Track>()
    var action: Action = Action.Pause()
}