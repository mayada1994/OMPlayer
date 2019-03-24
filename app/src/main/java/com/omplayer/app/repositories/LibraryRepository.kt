package com.omplayer.app.repositories

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.WorkerThread
import com.omplayer.app.db.entities.Album
import com.omplayer.app.db.entities.Artist
import com.omplayer.app.db.entities.Genre
import com.omplayer.app.db.entities.Track
import com.omplayer.app.di.SingletonHolder


class LibraryRepository(val context: Context) {

    private val TAG: String = this.javaClass.simpleName
    private val UNKNOWN: String = "Unknown"

    var genres = ArrayList<Genre>()
    var artists = ArrayList<Artist>()
    var albums = ArrayList<Album>()
    var tracks = ArrayList<Track>()
    val db = SingletonHolder.db
    private val formats = arrayOf(".aac", ".mp3", ".wav", ".ogg", ".midi", ".3gp", ".mp4", ".m4a", ".amr", ".flac")

    @WorkerThread
    suspend fun scanDeviceForGenres(): ArrayList<Genre> {
        val where: String? = null

        val columns = arrayOf(MediaStore.Audio.Genres.NAME)
        val sortOrder = MediaStore.Audio.Genres.NAME + " COLLATE LOCALIZED ASC"

        var cursor: Cursor? = null
        try {
            val uri = MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI
            cursor = context.contentResolver.query(uri, columns, where, null, sortOrder)
            if (cursor != null) {
                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
                    val name = if (cursor.getString(0) != null) cursor.getString(0) else UNKNOWN
                    cursor.moveToNext()
                    genres.add(Genre(name))
                }
            }

        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
        genres.add(Genre(UNKNOWN))
        return genres
    }

    @WorkerThread
    suspend fun scanDeviceForArtists(): ArrayList<Artist> {
        val where: String? = null
        val columns = arrayOf(
            MediaStore.Audio.Artists.ARTIST
        )
        val sortOrder =
            MediaStore.Audio.Artists.ARTIST + " COLLATE LOCALIZED ASC"

        var cursor: Cursor? = null
        try {
            val uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI
            cursor = context.contentResolver.query(uri, columns, where, null, sortOrder)
            if (cursor != null) {
                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
                    val name = if (cursor.getString(0) != null) cursor.getString(0) else UNKNOWN

                    cursor.moveToNext()
                    artists.add(Artist(name))
                }
            }

        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
        return artists
    }

    @WorkerThread
    suspend fun scanDeviceForAlbums(): ArrayList<Album> {
        val where: String? = null
        val columns = arrayOf(
            MediaStore.Audio.Albums.ALBUM,
            MediaStore.Audio.Albums.ARTIST,
            MediaStore.Audio.Albums.ALBUM_ART,
            MediaStore.Audio.Albums.FIRST_YEAR
        )
        val sortOrder =
            MediaStore.Audio.Albums.ARTIST + "," + MediaStore.Audio.Albums.ALBUM + " COLLATE LOCALIZED ASC"

        var cursor: Cursor? = null
        try {
            var containedAlbums: ArrayList<AlbumTag> = ArrayList()
            val uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI
            cursor = context.contentResolver.query(uri, columns, where, null, sortOrder)
            if (cursor != null) {
                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
                    val title = if (cursor.getString(0) != null) cursor.getString(0) else UNKNOWN
                    val artist = if (cursor.getString(1) != null) cursor.getString(1) else UNKNOWN
                    val albumArt = if (cursor.getString(2) != null) cursor.getString(2) else ""
                    val year = if (cursor.getString(3) != null) cursor.getString(3) else UNKNOWN

                    cursor.moveToNext()

                    if (!containedAlbums.contains(AlbumTag(artist, title, year))) {
                        val artistId = getArtistId(artist)
                        albums.add(Album(title, albumArt, year, artistId))
                        containedAlbums.add(AlbumTag(artist, title, year))
                    }
                }
            }

        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
        return albums
    }

    @WorkerThread
    suspend fun scanDeviceForTracks(): ArrayList<Track> {
        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
        val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.TRACK,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.YEAR,
            MediaStore.Audio.Media._ID
        )
        val sortOrder =
            MediaStore.Audio.AudioColumns.ARTIST + "," + MediaStore.Audio.AudioColumns.ALBUM + " COLLATE LOCALIZED ASC"

        var cursor: Cursor? = null
        try {
            val uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            cursor = context.contentResolver.query(uri, projection, selection, null, sortOrder)
            if (cursor != null) {
                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
                    val title = cursor.getString(0)
                    val artist = cursor.getString(1)
                    val path = cursor.getString(2)
                    val duration = cursor.getString(3)
                    val position = cursor.getString(4)
                    val album = cursor.getString(5)
                    val year = if (cursor.getString(6) != null) cursor.getString(6) else UNKNOWN
                    val id = cursor.getString(7)
                    var genre = ""

                    val genreUri = MediaStore.Audio.Genres.getContentUriForAudioId("external", id.toInt())
                    val genresCursor = context.contentResolver.query(
                        genreUri,
                        arrayOf(MediaStore.Audio.Genres.NAME), null, null, null
                    )
                    val genre_column_index = genresCursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.NAME)

                    if (genresCursor.moveToFirst()) {
                        do {
                            genre = genresCursor.getString(genre_column_index)
                        } while (genresCursor.moveToNext())
                    }
                    if (genresCursor != null) {
                        genresCursor.close()
                    }

                    cursor.moveToNext()
                    if (path != null && supportedFormat(path)) {
                        if (genre.isEmpty()) {
                            genre = UNKNOWN
                        }
                        val genreId = getGenreId(genre)
                        val albumId = getAlbumId(album, artist, year)

                        tracks.add(
                            Track(
                                title,
                                position,
                                duration.toInt(),
                                albumId,
                                genreId,
                                path
                            )
                        )
                    }
                }
            }

        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
        return tracks
    }

    @WorkerThread
    private suspend fun supportedFormat(path: String): Boolean {
        for (format in formats) {
            if (path.endsWith(format)) {
                return true
            }
        }
        return false
    }

    @WorkerThread
    private suspend fun getGenreId(name: String): Int {
        return db.genreDao().getGenreByName(name).id
    }

    @WorkerThread
    private suspend fun getArtistId(name: String): Int {
        return db.artistDao().getArtistByName(name).id
    }

    @WorkerThread
    private suspend fun getAlbumId(title: String, artist: String, year: String): Int {
        val artistId = getArtistId(artist)
        return try {
            db.albumDao().getAlbumByTrack(artistId, title, year).id
        } catch (e: Exception) {
            db.albumDao().getAlbumByTitle(title).id
        }
    }
}

data class AlbumTag(
    var artist: String = "",
    var title: String = "",
    var year: String = ""
)