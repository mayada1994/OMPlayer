package com.omplayer.app.services.lastFmService

import okhttp3.ResponseBody
import com.omplayer.app.entities.LastFmSessionWrapper
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface LastFmService {

    @POST("?method=auth.getMobileSession")
    fun getSession(
        @Query("api_key") apiKey: String,
        @Query("password") password: String,
        @Query("username") username: String,
        @Query("api_sig") api_sig: String,
        @Query("format") format: String
    ): Call<LastFmSessionWrapper>

    @POST("?method=track.updateNowPlaying")
    fun updatePlayingTrack(
        @Query("album") album: String,
        @Query("artist") artist: String,
        @Query("track") track: String,
        @Query("api_key") apiKey: String,
        @Query("api_sig") api_sig: String,
        @Query("sk") sessionKey: String,
        @Query("format") format: String
    ): Call<ResponseBody>
}