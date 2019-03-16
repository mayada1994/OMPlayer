package com.example.android.omplayer.utils

import com.example.android.omplayer.di.SingletonHolder
import android.content.Context.MODE_PRIVATE


object PreferenceUtil {
    private val UPDATE_DB: String = "need_to_update_db"
    private val context = SingletonHolder.application
    private val APP_PREFERENCES = "com.example.android.omplayer.player_preferences"

    var sharedPreferences = context.getSharedPreferences(
        APP_PREFERENCES, MODE_PRIVATE
    )

    var updateLibrary: Boolean
        get() = sharedPreferences.getBoolean(UPDATE_DB, true)
        set(value) = sharedPreferences.edit().putBoolean(UPDATE_DB, value).apply()
}