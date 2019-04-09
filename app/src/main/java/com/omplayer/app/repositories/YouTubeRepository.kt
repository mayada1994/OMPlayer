package com.omplayer.app.repositories

import com.omplayer.app.services.youTubeService.YouTubeService
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit

class YouTubeRepository {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.last.fm/music/")
        .build()

    private val youTubeService = retrofit.create(YouTubeService::class.java)

    fun getData(artist:String, album:String, song:String): Call<ResponseBody> {
        return youTubeService.getHtml(artist, album, song)
    }

    fun getData(url :String): Call<ResponseBody> {
        return youTubeService.getHtml(url)
    }

}