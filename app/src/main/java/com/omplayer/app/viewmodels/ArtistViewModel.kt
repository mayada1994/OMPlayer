package com.omplayer.app.viewmodels

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.siyamed.shapeimageview.RoundedImageView
import com.mikhaellopez.circularimageview.CircularImageView
import com.omplayer.app.R
import com.omplayer.app.db.entities.Album
import com.omplayer.app.db.entities.Track
import com.omplayer.app.di.SingletonHolder
import com.omplayer.app.di.SingletonHolder.application
import com.omplayer.app.fragments.ArtistFragment
import com.omplayer.app.repositories.AlbumRepository
import com.omplayer.app.repositories.TrackRepository
import com.omplayer.app.utils.LibraryUtil
import kotlinx.coroutines.*
import java.io.File
import kotlin.coroutines.CoroutineContext

class ArtistViewModel(application: Application) : AndroidViewModel(application) {
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.IO
    private val scope = CoroutineScope(coroutineContext)
    private val db = SingletonHolder.db

    private val albumRepository: AlbumRepository = AlbumRepository(db.albumDao())
    private val trackRepository: TrackRepository = TrackRepository(db.trackDao())
    private var albums = ArrayList<Album>()
    private var tracks = ArrayList<Track>()

    fun loadArtistAlbums(artistId: Int, fragment: ArtistFragment) {
        scope.launch {
            withContext(coroutineContext) {
                tracks = trackRepository.getTracksByArtistId(artistId) as ArrayList<Track>
                tracks.forEach { track ->
                    val currentAlbum: Album = albumRepository.getAlbumById(track.albumId)!!
                    if (!albums.contains(currentAlbum)) {
                        albums.add(currentAlbum)
                    }
                }
                albums.sortBy { it.year }
            }
            withContext(Dispatchers.Main) {
                LibraryUtil.selectedArtistAlbumList = albums
                fragment.selectArtist()
            }
        }
    }

    fun getArtistName(): String {
        return LibraryUtil.artists[LibraryUtil.selectedArtist].name
    }

    fun getArtistCover(): String {
        var image = LibraryUtil.artists[LibraryUtil.selectedArtist].image
        if (image.isEmpty()) {
            val path = SingletonHolder.application.filesDir.absolutePath
            val file = File(path, "${getArtistName()}.png")
            if(file.exists()) {
                image = file.absolutePath
                setArtistCover(image)
            }
        }
        return image
    }

    private fun setArtistCover(cover: String) {
        LibraryUtil.artists[LibraryUtil.selectedArtist].image = cover
    }

    fun loadImage(imageView: RoundedImageView){
        val file = File(getArtistCover())
        val uri = Uri.fromFile(file)

        Glide.with(application).load(uri)
            .apply(RequestOptions().placeholder(R.drawable.placeholder).error(R.drawable.placeholder))
            .into(imageView)
    }
}