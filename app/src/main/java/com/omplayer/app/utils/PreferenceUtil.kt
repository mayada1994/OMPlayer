package com.omplayer.app.utils

import com.omplayer.app.di.SingletonHolder
import android.content.Context.MODE_PRIVATE


object PreferenceUtil {
    private val UPDATE_DB: String = "need_to_update_db"
    private val context = SingletonHolder.application
    private val APP_PREFERENCES = "com.omplayer.app.player_preferences"

    var sharedPreferences = context.getSharedPreferences(
        APP_PREFERENCES, MODE_PRIVATE
    )

    var updateLibrary: Boolean
        get() = sharedPreferences.getBoolean(UPDATE_DB, true)
        set(value) = sharedPreferences.edit().putBoolean(UPDATE_DB, value).apply()
}