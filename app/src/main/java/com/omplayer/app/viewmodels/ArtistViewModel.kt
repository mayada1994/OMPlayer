package com.omplayer.app.viewmodels

import android.app.Application
import android.net.Uri
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.siyamed.shapeimageview.RoundedImageView
import com.omplayer.app.R
import com.omplayer.app.adapters.ArtistAdapter
import com.omplayer.app.di.SingletonHolder
import com.omplayer.app.di.SingletonHolder.application
import com.omplayer.app.repositories.AlbumRepository
import com.omplayer.app.repositories.TrackRepository
import com.omplayer.app.utils.LibraryUtil
import kotlinx.coroutines.*
import java.io.File
import kotlin.coroutines.CoroutineContext

class ArtistViewModel(application: Application) : AndroidViewModel(application), ArtistAdapter.Callback {

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.IO
    private val scope = CoroutineScope(coroutineContext)
    private val db = SingletonHolder.db

    private val albumRepository: AlbumRepository = AlbumRepository(db.albumDao())
    private val trackRepository: TrackRepository = TrackRepository(db.trackDao())

    private val _viewLiveData: MutableLiveData<View> = MutableLiveData()
    val viewLiveData = _viewLiveData

    val itemAdapter = ArtistAdapter(LibraryUtil.artists, this)


    override fun loadArtistAlbums(artistId: Int, view: View) {
        scope.launch {
            LibraryUtil.selectedArtistAlbumList = trackRepository.getTracksByArtistId(artistId).mapNotNull {
                albumRepository.getAlbumById(it.albumId)
            }.distinctBy { it.id }.sortedBy { it.year }

            withContext(Dispatchers.Main) {
                _viewLiveData.value = view
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