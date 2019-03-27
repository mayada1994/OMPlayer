package com.omplayer.app.services.lyricsService

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface LyricsService {

    @GET
    fun getHtml(@Url url: String): Call<ResponseBody>
}