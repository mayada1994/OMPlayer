package com.omplayer.app.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.omplayer.app.repositories.YouTubeRepository
import com.omplayer.app.utils.LibraryUtil
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class VideoViewModel(application: Application) : AndroidViewModel(application) {

    private val NOT_FOUND = "Not Found"
    private val YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v="

    fun getVideoId(artist: String, album: String, song: String) {
        LibraryUtil.selectedTrackVideoId = NOT_FOUND
        val url = generateLastFmUrl(artist, album, song)
        val youTubeRepository = YouTubeRepository(url)
        youTubeRepository.getData().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                try {
                    val htmlBody = response.body()!!.string()

                    val pattern = getPattern()
                    val matcher = pattern.matcher(htmlBody)
                    var matchStart = 0
                    var matchEnd = 1
                    while (matcher.find()) {
                        matchStart = matcher.start(1)
                        matchEnd = matcher.end()

                        val resultUrl = htmlBody.substring(matchStart, matchEnd)
                        if (resultUrl.contains(YOUTUBE_BASE_URL)) {
                            LibraryUtil.selectedTrackVideoId = extractVideoId(resultUrl)
                        }
                    }

                } catch (e: NullPointerException) {
                    Log.e("YouTubeResponse", response.message())
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("YouTubeResponse", "Profile not found")
            }
        })
    }

    private fun generateLastFmUrl(artist: String, album: String, song: String): String {
        val baseUrl = "https://www.last.fm/music/"
        val currentArtist = artist.replace(" ", "+")
        val currentAlbum = album.replace(" ", "+")
        val currentTitle = song.replace(" ", "+")
        return "$baseUrl$currentArtist/$currentAlbum/$currentTitle/".replace("#", "%23")
    }

    private fun getPattern(): Pattern{
        return Pattern.compile(
            "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
                    + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                    + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
            Pattern.CASE_INSENSITIVE or Pattern.MULTILINE or Pattern.DOTALL
        )
    }

    private fun extractVideoId(youTubeUrl: String): String {
        return youTubeUrl.replace(YOUTUBE_BASE_URL, "")
    }
}