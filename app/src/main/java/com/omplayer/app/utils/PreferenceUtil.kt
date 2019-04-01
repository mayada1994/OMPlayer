package com.omplayer.app.utils

import com.omplayer.app.di.SingletonHolder
import android.content.Context.MODE_PRIVATE
import com.google.gson.Gson
import com.omplayer.app.entities.LastFmSession


object PreferenceUtil {

    private val context = SingletonHolder.application
    private const val UPDATE_DB: String = "need_to_update_db"
    private const val CURRENT_LAST_FM_SESSION: String = "current_last_fm_session"
    private const val APP_PREFERENCES = "com.omplayer.app.player_preferences"
    private val gson: Gson = Gson()

    private var sharedPreferences = context.getSharedPreferences(
        APP_PREFERENCES, MODE_PRIVATE
    )

    var updateLibrary: Boolean
        get() = sharedPreferences.getBoolean(UPDATE_DB, true)
        set(value) = sharedPreferences.edit().putBoolean(UPDATE_DB, value).apply()

    var currentLastFmSession: LastFmSession?
        get() = gson.fromJson(sharedPreferences.getString(CURRENT_LAST_FM_SESSION, null), LastFmSession::class.java)
        set(value) = sharedPreferences.edit().putString(CURRENT_LAST_FM_SESSION, gson.toJson(value)).apply()
}