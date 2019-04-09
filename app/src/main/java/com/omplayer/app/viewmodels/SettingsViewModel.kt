package com.omplayer.app.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.omplayer.app.dialogFragments.LastFmLoginDialogFragment
import com.omplayer.app.fragments.SettingsFragment

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    fun login(fragment: SettingsFragment) {
        val lastFmLoginDialog = LastFmLoginDialogFragment.newInstance(fragment)
        lastFmLoginDialog.show(fragment.fragmentManager, "")
    }

}