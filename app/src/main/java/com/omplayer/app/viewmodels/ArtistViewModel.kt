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
import com.omplayer.app.db.entities.Artist
import com.omplayer.app.di.SingletonHolder
import com.omplayer.app.di.SingletonHolder.application
import com.omplayer.app.repositories.AlbumRepository
import com.omplayer.app.repositories.TrackRepository
import com.omplayer.app.utils.LibraryUtil
import kotlinx.coroutines.*
import java.io.File
import kotlin.coroutines.CoroutineContext

class ArtistViewModel(application: Application) : BaseViewModel(application), ArtistAdapter.Callback {

//    private var parentJob = Job()
//    private val coroutineContext: CoroutineContext
//        get() = parentJob + Dispatchers.IO
//    private val scope = CoroutineScope(coroutineContext)
    private val db = SingletonHolder.db

    private val albumRepository: AlbumRepository = AlbumRepository(db.albumDao())
    private val trackRepository: TrackRepository = TrackRepository(db.trackDao())

    private val _viewLiveData: MutableLiveData<View> = MutableLiveData()
    val viewLiveData = _viewLiveData

    val itemAdapter = ArtistAdapter(LibraryUtil.artists, this)


    override fun loadArtistAlbums(artistId: Int, view: View) {
        launch {
            LibraryUtil.selectedArtistAlbumList = trackRepository.getTracksByArtistId(artistId).mapNotNull {
                albumRepository.getAlbumById(it.albumId)
            }.distinctBy { it.id }.sortedBy { it.year }

            withContext(Dispatchers.Main) {
                _viewLiveData.value = view
            }
        }
    }

    fun getArtist(): Artist {
        return LibraryUtil.artists[LibraryUtil.selectedArtist]
    }

    fun loadImage(imageView: RoundedImageView){
        val artist = getArtist()
        val file = File(artist.image)
        val uri = Uri.fromFile(file)

        Glide.with(application).load(uri)
            .apply(RequestOptions().placeholder(R.drawable.placeholder).error(R.drawable.placeholder))
            .into(imageView)
    }
}