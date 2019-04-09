package com.omplayer.app.repositories

import com.omplayer.app.entities.LastFmArtistWrapper
import com.omplayer.app.entities.LastFmSessionWrapper
import com.omplayer.app.entities.LastFmSimilarArtistsWrapper
import com.omplayer.app.entities.LastFmUserWrapper
import com.omplayer.app.services.lastFmService.LastFmService
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LastFmRepository {

    private val FORMAT = "json"

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://ws.audioscrobbler.com/2.0/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val rawRetrofit = Retrofit.Builder()
        .baseUrl("https://ws.audioscrobbler.com/2.0/")
        .build()

    private val lastFmService = retrofit.create(LastFmService::class.java)

    private val rawLastFmService = rawRetrofit.create(LastFmService::class.java)

    fun getLastFmSession(
        apiKey: String,
        password: String,
        username: String,
        api_sig: String
    ): Call<LastFmSessionWrapper> {
        return lastFmService.getSession(apiKey, password, username, api_sig, FORMAT)
    }

    fun updatePlayingTrack(
        album: String,
        artist: String,
        track: String,
        apiKey: String,
        api_sig: String,
        sk: String
    ): Call<ResponseBody> {
        return rawLastFmService.updatePlayingTrack(album, artist, track, apiKey, api_sig, sk, FORMAT)
    }

    fun scrobbleTrack(
        album: String,
        artist: String,
        track: String,
        timestamp: String,
        apiKey: String,
        api_sig: String,
        sk: String
    ): Call<ResponseBody> {
        return rawLastFmService.scrobbleTrack(album, artist, track, timestamp, apiKey, api_sig, sk, FORMAT)
    }

    fun loveTrack(
        artist: String,
        track: String,
        apiKey: String,
        api_sig: String,
        sk: String
    ): Call<ResponseBody> {
        return rawLastFmService.loveTrack(artist, track, apiKey, api_sig, sk, FORMAT)
    }

    fun getUserInfo(user: String, apiKey: String): Call<LastFmUserWrapper> {
        return lastFmService.getUserInfo(user, apiKey, FORMAT)
    }

    fun getArtistInfo(artist: String, apiKey: String): Call<LastFmArtistWrapper> {
        return lastFmService.getArtistInfo(artist, apiKey, FORMAT)
    }

    fun getSimilarArtists(artist: String, apiKey: String): Call<LastFmSimilarArtistsWrapper> {
        return lastFmService.getSimilarArtists(artist, apiKey, FORMAT)
    }

}