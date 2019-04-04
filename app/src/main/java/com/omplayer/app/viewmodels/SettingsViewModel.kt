package com.omplayer.app.viewmodels

import android.app.Application
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.AndroidViewModel
import com.omplayer.app.dialogFragments.LastFmLoginDialogFragment

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    fun login(fragmentManager: FragmentManager) {
        val lastFmLoginDialog = LastFmLoginDialogFragment.newInstance()
        lastFmLoginDialog.show(fragmentManager, "")
    }

}