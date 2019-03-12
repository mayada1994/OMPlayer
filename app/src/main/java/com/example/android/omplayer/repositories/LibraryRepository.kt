package com.example.android.omplayer.repositories

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.util.Log
import com.example.android.omplayer.db.entities.Album
import com.example.android.omplayer.db.entities.Artist
import com.example.android.omplayer.db.entities.Genre
import com.example.android.omplayer.db.entities.Track
import android.media.MediaMetadataRetriever
import com.example.android.omplayer.db.PlayerDatabase
import com.example.android.omplayer.entities.LibraryUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LibraryRepository(val context: Context) {

    private val TAG: String = this.javaClass.simpleName
    var genres = ArrayList<Genre>()
    var artists = ArrayList<Artist>()
    var albums = ArrayList<Album>()
    var tracks = ArrayList<Track>()
    private val formats = arrayOf(".aac", ".mp3", ".wav", ".ogg", ".midi", ".3gp", ".mp4", ".m4a", ".amr", ".flac")

    fun prepareDataForDb() {
        try {
            val genreRepository = GenreRepository(context)
            CoroutineScope(Dispatchers.IO).launch {
                withContext(coroutineContext) {
                    genres = scanDeviceForGenres()
                    LibraryUtil.genres = genres
                    for (genre in LibraryUtil.genres) {
                        genreRepository.insertGenre(genre)
                    }
                }

                withContext(coroutineContext) {
                    artists = scanDeviceForArtists()
                    LibraryUtil.artists = artists
                }

                withContext(coroutineContext) {
                    albums = scanDeviceForAlbums()
                    LibraryUtil.albums = albums
                }

                withContext(coroutineContext) {
                    tracks = scanDeviceForTracks()
                    LibraryUtil.tracks = tracks
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, e.message)
        }
    }

    private fun scanDeviceForGenres(): ArrayList<Genre> {
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
                    val name = if (cursor.getString(0) != null) cursor.getString(0) else "Unknown"

                    cursor.moveToNext()
                    genres.add(Genre(name))
                    PlayerDatabase.getDatabase(context).genreDao().insert(Genre(name))
                }
            }

        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
        return genres
    }

    private fun scanDeviceForArtists(): ArrayList<Artist> {
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
                    val name = if (cursor.getString(0) != null) cursor.getString(0) else "Unknown"

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

    private fun scanDeviceForAlbums(): ArrayList<Album> {
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
                    val title = if (cursor.getString(0) != null) cursor.getString(0) else "Unknown"
                    val artist = if (cursor.getString(1) != null) cursor.getString(1) else "Unknown"
                    val albumart = if (cursor.getString(2) != null) cursor.getString(2) else ""
                    val year = if (cursor.getString(3) != null) cursor.getString(3) else "0"

                    cursor.moveToNext()

                    if (!containedAlbums.contains(AlbumTag(artist, title, year))) {
                        albums.add(Album(title, albumart, year.toInt(), getArtistId(artist)))
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

    private fun scanDeviceForTracks(): ArrayList<Track> {
        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
        val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.TRACK,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.YEAR
        )
        val sortOrder =
            MediaStore.Audio.AudioColumns.ARTIST + "," + MediaStore.Audio.AudioColumns.ALBUM + " COLLATE LOCALIZED ASC"

        var cursor: Cursor? = null
        try {
            val media = MediaMetadataRetriever()
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
                    val year = cursor.getString(6)
                    media.setDataSource(path)
                    val genre = media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE)
                    cursor.moveToNext()
                    if (path != null && supportedFormat(path)) {
                        tracks.add(
                            Track(
                                title,
                                position,
                                duration.toInt(),
                                getAlbumId(album, getArtistId(artist), year.toInt()),
                                getGenreId(genre),
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

    private fun supportedFormat(path: String): Boolean {
        for (format in formats) {
            if (path.endsWith(format)) {
                return true
            }
        }
        return false
    }

    private fun getGenreId(name: String): Int {
        for (genre in genres) {
            if (genre.name == name) {
                return genre.id
            }
        }
        return 0
    }

    private fun getArtistId(name: String): Int {
        for (artist in artists) {
            if (artist.name == name) {
                return artist.id
            }
        }
        return 0
    }

    private fun getAlbumId(title: String, artistId: Int, year: Int): Int {
        for (album in albums) {
            if (album.title == title && album.artistId == artistId && album.year == year) {
                return album.id
            }
        }
        return 0
    }

}

data class AlbumTag(
    var artist: String = "",
    var title: String = "",
    var year: String = ""
)