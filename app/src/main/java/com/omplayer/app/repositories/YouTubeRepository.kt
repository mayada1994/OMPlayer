package com.omplayer.app.repositories

import com.omplayer.app.services.youTubeService.YouTubeService
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit

class YouTubeRepository(url:String) {

    private val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .build()

    private val youTubeService = retrofit.create(YouTubeService::class.java)

    fun getData(): Call<ResponseBody> {
        return youTubeService.getHtml(this.retrofit.baseUrl().toString())
    }

}