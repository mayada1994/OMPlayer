package com.omplayer.app.viewmodels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.AndroidViewModel
import com.omplayer.app.dialogFragments.LyricsDialogFragment
import com.omplayer.app.repositories.LyricsRepository
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LyricsViewModel(application: Application) : AndroidViewModel(application) {

    private val NOT_FOUND = "Lyrics Not Found"

    fun getSongLyrics(artist: String, song: String, fragmentManager: FragmentManager) {
        var lyrics: String
        val regex = Regex("[^A-Za-z0-9]+")
        var currentArtist = artist.replace(regex, "").toLowerCase()
        val currentTitle = song.replace(regex, "").toLowerCase()
        if (currentArtist.startsWith("the")) {
            currentArtist = currentArtist.substring(3)
        }
        val lyricsRepository = LyricsRepository()
        lyricsRepository.getLyrics("$currentArtist/$currentTitle.html").enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val upperString =
                        "<!-- Usage of azlyrics.com content by any third-party lyrics provider is prohibited by our licensing agreement. Sorry about that. -->"
                    val list = response.body()!!.string()
                    val start =
                        list.indexOf(upperString) + upperString.length
                    val end = list.indexOf("<!-- MxM banner -->")
                    lyrics = list.substring(start, end)
                    lyrics = lyrics.replace("<br>", "").replace("</br>", "").replace("</div>", "").replace("<i>", "")
                        .replace("</i>", "")
                    if (lyrics.isEmpty()) {
                        lyrics = NOT_FOUND
                    }
                    showLyrics(lyrics, fragmentManager)
                } catch (e: Exception) {
                    Log.e("LyricsResponse", response.message())
                    Toast.makeText(this@LyricsViewModel.getApplication(), NOT_FOUND, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("LyricsResponse", t.message)
                if (t.message!!.contains("443")) {
                    Toast.makeText(this@LyricsViewModel.getApplication(), "Service Unavailable", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@LyricsViewModel.getApplication(), NOT_FOUND, Toast.LENGTH_LONG).show()
                }
            }

        })
    }

    fun showLyrics(lyrics: String, fragmentManager: FragmentManager) {
        val lyricsDialog = LyricsDialogFragment.newInstance(lyrics)
        lyricsDialog.show(fragmentManager, "")
    }
}