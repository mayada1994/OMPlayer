package com.omplayer.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.omplayer.app.R
import com.omplayer.app.di.SingletonHolder
import com.omplayer.app.utils.PreferenceUtil
import com.omplayer.app.viewmodels.LastFmViewModel
import com.omplayer.app.viewmodels.SettingsViewModel
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    private val settingsViewModel = SettingsViewModel(SingletonHolder.application)
    private val lastFmViewModel = LastFmViewModel(SingletonHolder.application)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeUserProfile()
        scrobble_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                enableScrobbling()
            } else {
                PreferenceUtil.scrobble = false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        initializeUserProfile()
    }

    private fun enableScrobbling() {
        if (PreferenceUtil.currentLastFmSession != null) {
            PreferenceUtil.scrobble = true
        } else {
            settingsViewModel.login(fragmentManager!!)
            scrobble_switch.isChecked = false
        }
    }

    fun initializeUserProfile() {
        if (PreferenceUtil.currentLastFmSession != null) {
            scrobble_switch.isChecked = PreferenceUtil.scrobble == true
            val session = PreferenceUtil.currentLastFmSession
            val user = session!!.name
            user_username.text = user
            lastFmViewModel.getUserInfo(user, img_user_profile)
        }
    }

}