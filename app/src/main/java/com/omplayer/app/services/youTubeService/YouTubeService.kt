package com.omplayer.app.services.youTubeService

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface YouTubeService {

    @GET("{artist}/{album}/{song}/")
    fun getHtml(@Path("artist") artist: String, @Path("album") album: String, @Path("song") song: String): Call<ResponseBody>

    @GET
    fun getHtml(@Url url: String): Call<ResponseBody>
}