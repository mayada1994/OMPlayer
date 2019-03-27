package com.omplayer.app.repositories

import com.omplayer.app.services.lyricsService.LyricsService
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit

class LyricsRepository {

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://azlyrics.com/lyrics/")
        .build()

    private val lyricsService = retrofit.create(LyricsService::class.java)

    fun getLyrics(url: String): Call<ResponseBody> {
        return lyricsService.getHtml(url)
    }
}