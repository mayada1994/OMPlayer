package com.omplayer.app.viewmodels

import android.app.Application
import android.net.Uri
import android.view.View
import android.widget.TextView
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

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
//            LibraryUtil.selectedArtistAlbumList = trackRepository.getTracksByArtistId(artistId).mapNotNull {
//                albumRepository.getAlbumById(it.albumId)
//            }.distinctBy { it.id }.sortedBy { it.year }

            LibraryUtil.selectedArtistAlbumList = db.albumDao().getArtistAlbums(artistId)
            withContext(Dispatchers.Main) {
                _viewLiveData.value = view
            }
        }
    }

    fun getArtist(): Artist {
        return LibraryUtil.artists[LibraryUtil.selectedArtist]
    }

    fun setArtistInfo(songsNum: TextView, maxYear: TextView, maxYearAlbum: TextView, avgYear: TextView) {
        launch {
            val artistId = getArtist().id
            val mSongsNum = db.trackDao().getArtistTrackNumber(artistId)
            val mMaxYear = db.albumDao().getArtistAlbumsMaxYear(artistId)
            val mMaxYearAlbum = db.albumDao().getArtistAlbumsWithMaxYear(artistId)[0]
            val mAvgYear = db.albumDao().getArtistAlbumsAvgYear(artistId)
            withContext(Dispatchers.Main) {
                songsNum.text = "Artist songs amount: $mSongsNum"
                maxYear.text = "Artist max year in albums: $mMaxYear"
                maxYearAlbum.text = "Artist max year album name: ${mMaxYearAlbum.title}"
                avgYear.text = "Artist avg year per album: $mAvgYear"
            }
        }
    }

    fun loadImage(imageView: RoundedImageView) {
        launch {
            val artist = db.artistDao().getArtistById(getArtist().id)
            withContext(Dispatchers.Main){
                val file = File(artist.image)
                val uri = Uri.fromFile(file)

                Glide.with(application).load(uri)
                    .apply(RequestOptions().placeholder(R.drawable.placeholder).error(R.drawable.placeholder))
                    .into(imageView)
            }
        }
    }
}