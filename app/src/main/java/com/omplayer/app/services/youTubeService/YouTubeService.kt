package com.omplayer.app.services.youTubeService

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface YouTubeService {

    @GET
    fun getHtml(@Url url: String): Call<ResponseBody>
}