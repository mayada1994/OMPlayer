package com.omplayer.app.viewmodels

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.omplayer.app.R
import com.omplayer.app.adapters.GenreAdapter
import com.omplayer.app.db.entities.Track
import com.omplayer.app.di.SingletonHolder
import com.omplayer.app.fragments.GenreFragment
import com.omplayer.app.repositories.TrackRepository
import com.omplayer.app.utils.LibraryUtil
import com.omplayer.app.utils.LibraryUtil.genres
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class GenreViewModel(application: Application) : AndroidViewModel(application), GenreAdapter.Callback {

    val genreAdapter = GenreAdapter(LibraryUtil.genres, this)

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.IO
    private val scope = CoroutineScope(coroutineContext)
    private val db = SingletonHolder.db

    private var _viewLiveData: MutableLiveData<View> = MutableLiveData()
    var viewLiveData = _viewLiveData





    private val trackRepository: TrackRepository = TrackRepository(db.trackDao())
    private var tracks = ArrayList<Track>()


    override fun loadGenreTracks(genreId: Int, view: View) {
        scope.launch {
            withContext(coroutineContext) {
                tracks = trackRepository.getTracksByGenreId(genreId) as ArrayList<Track>
                withContext(Dispatchers.Main) {
                    LibraryUtil.selectedGenreTracklist = tracks
                    _viewLiveData.value = view
                }
            }
        }
    }

//    fun loadGenreTracks(genreId: Int, fragment:GenreFragment) {
//        scope.launch {
//            withContext(coroutineContext) {
//                tracks = trackRepository.getTracksByGenreId(genreId) as ArrayList<Track>
//                withContext(Dispatchers.Main) {
//                    LibraryUtil.selectedGenreTracklist = tracks
//                    fragment.selectGenre()
//                }
//            }
//        }
//    }

    fun getGenreName():String{
        return LibraryUtil.genres[LibraryUtil.selectedGenre].name
    }
}