package com.omplayer.app.viewmodels

import android.app.Application
import android.net.Uri
import android.widget.EditText
import androidx.lifecycle.AndroidViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mikhaellopez.circularimageview.CircularImageView
import com.omplayer.app.R
import com.omplayer.app.di.SingletonHolder.application
import com.omplayer.app.di.SingletonHolder.db
import com.omplayer.app.repositories.EditTagRepository
import com.omplayer.app.utils.LibraryUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class EditTagsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = EditTagRepository()

    private val currentTrack = LibraryUtil.tracklist[LibraryUtil.selectedTrack]

    var path = ""

    private fun getTrackTitle(): String {
        return currentTrack.title
    }

    private fun getTrackAlbum(): String {
        return repository.getTrackAlbum(currentTrack)
    }

    private fun getTrackArtist(): String {
        return repository.getTrackArtist(currentTrack)
    }

    private fun getTrackPosition(): Int {
        return currentTrack.id
    }

    private fun getTrackGenre(): String {
        return repository.getTrackGenre(currentTrack)
    }

    fun loadTrackAlbumCover(imageView: CircularImageView) {
        val file = File(path)
        val uri = Uri.fromFile(file)

        Glide.with(application).load(uri)
            .apply(RequestOptions().placeholder(R.drawable.placeholder).error(R.drawable.placeholder))
            .into(imageView)
    }

    fun initViews(
        trackTitleView: EditText,
        trackAlbumView: EditText,
        trackArtistView: EditText,
        trackPositionView: EditText,
        trackGenreView: EditText
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(coroutineContext) {
                path = repository.getTrackAlbumCover(currentTrack)
                val trackTitle = getTrackTitle()
                val trackAlbum = getTrackAlbum()
                val trackArtist = getTrackArtist()
                val trackPosition = getTrackPosition()
                val trackGenre = getTrackGenre()
                withContext(Dispatchers.Main) {
                    trackTitleView.setText(trackTitle)
                    trackAlbumView.setText(trackAlbum)
                    trackArtistView.setText(trackArtist)
                    trackPositionView.setText(trackPosition)
                    trackGenreView.setText(trackGenre)
                }
            }
        }
    }

    fun updateTrack(title: String, album: String, artist: String, position: Int, genre: String) {

        CoroutineScope(Dispatchers.IO).launch {
            withContext(coroutineContext) {
                currentTrack.title = title
                currentTrack.albumId = repository.getAlbumId(album, artist, "0")
                currentTrack.artistId = repository.getArtistId(artist)
                currentTrack.position = position
                currentTrack.genreId = repository.getGenreId(genre)
                val path = application.filesDir.absolutePath
                val file = File(path, "${album + "_" + artist}.jpg")
                db.albumDao().getAlbumById(currentTrack.albumId).cover = file.absolutePath
            }
        }

    }
}