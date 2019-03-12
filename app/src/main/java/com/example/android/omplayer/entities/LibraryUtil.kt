
package com.example.android.omplayer.entities

import com.example.android.omplayer.db.entities.Album
import com.example.android.omplayer.db.entities.Artist
import com.example.android.omplayer.db.entities.Genre
import com.example.android.omplayer.db.entities.Track

object LibraryUtil {
    var selectedTrack = 0
    var tracklist = ArrayList<String>()
    var genres = ArrayList<Genre>()
    var artists = ArrayList<Artist>()
    var albums = ArrayList<Album>()
    var tracks = ArrayList<Track>()
}