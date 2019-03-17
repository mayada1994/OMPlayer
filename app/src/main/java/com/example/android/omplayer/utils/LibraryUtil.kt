
package com.example.android.omplayer.utils

import com.example.android.omplayer.db.entities.Album
import com.example.android.omplayer.db.entities.Artist
import com.example.android.omplayer.db.entities.Genre
import com.example.android.omplayer.db.entities.Track
import com.example.android.omplayer.stateMachine.Action

object LibraryUtil {
    var selectedTrack = 0
    var tracklist = ArrayList<Track>()
    var genres = ArrayList<Genre>()
    var artists = ArrayList<Artist>()
    var albums = ArrayList<Album>()
    var tracks = ArrayList<Track>()
    var action:Action = Action.Pause()
}