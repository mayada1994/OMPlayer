package com.omplayer.app.services.lyricsService

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface LyricsService {

    @GET("{artist}/{song}.html")
    fun getHtml(@Path("artist") artist: String, @Path("song") song: String): Call<ResponseBody>
}