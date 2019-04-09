package com.omplayer.app.services.lastFmService

import com.omplayer.app.entities.LastFmArtistWrapper
import com.omplayer.app.entities.LastFmSessionWrapper
import com.omplayer.app.entities.LastFmSimilarTracksWrapper
import com.omplayer.app.entities.LastFmUserWrapper
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
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

    @POST("?method=track.scrobble")
    fun scrobbleTrack(
        @Query("album") album: String,
        @Query("artist") artist: String,
        @Query("track") track: String,
        @Query("timestamp") timestamp: String,
        @Query("api_key") apiKey: String,
        @Query("api_sig") api_sig: String,
        @Query("sk") sessionKey: String,
        @Query("format") format: String
    ): Call<ResponseBody>

    @POST("?method=track.love")
    fun loveTrack(
        @Query("artist") artist: String,
        @Query("track") track: String,
        @Query("api_key") apiKey: String,
        @Query("api_sig") api_sig: String,
        @Query("sk") sessionKey: String,
        @Query("format") format: String
    ): Call<ResponseBody>

    @GET("?method=user.getInfo")
    fun getUserInfo(
        @Query("user") user: String,
        @Query("api_key") apiKey: String,
        @Query("format") format: String
    ): Call<LastFmUserWrapper>

    @GET("?method=artist.getInfo")
    fun getArtistInfo(
        @Query("artist") artist: String,
        @Query("api_key") apiKey: String,
        @Query("format") format: String
    ): Call<LastFmArtistWrapper>

    @GET("?method=track.getSimilar")
    fun getSimilarTracks(
        @Query("track") track: String,
        @Query("artist") artist: String,
        @Query("api_key") apiKey: String,
        @Query("format") format: String
    ): Call<LastFmSimilarTracksWrapper>

}