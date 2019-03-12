package com.example.android.omplayer.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.android.omplayer.db.entities.Genre
import com.example.android.omplayer.repositories.GenreRepository

class GenreViewModel(val context: Context){

    private val genreRepository: GenreRepository = GenreRepository(context)

    fun getGenresFromDb(): LiveData<List<Genre>>{
        return genreRepository.getAllGenres()!!
    }

}