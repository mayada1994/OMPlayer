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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

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

        button_log.setOnClickListener {
            if (PreferenceUtil.currentLastFmSession != null) {
                PreferenceUtil.currentLastFmSession = null
                initializeUserProfile()
            }else{
                settingsViewModel.login(this@SettingsFragment)
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
            settingsViewModel.login(this@SettingsFragment)
            scrobble_switch.isChecked = false
        }
    }

    fun initializeUserProfile() {
        if (PreferenceUtil.currentLastFmSession != null) {
            button_log.text = getString(R.string.last_fm_logout)
            scrobble_switch.isChecked = PreferenceUtil.scrobble == true
            val session = PreferenceUtil.currentLastFmSession
            val user = session!!.name
            user_username.text = user
            lastFmViewModel.getUserInfo(user, img_user_profile)
        }else{
            button_log.text = getString(R.string.last_fm_login)
            user_username.text = getString(R.string.unauthorised_last_fm_user)
            img_user_profile.setImageResource(R.drawable.ic_last_fm_placeholder)
            PreferenceUtil.scrobble = false
            scrobble_switch.isChecked = false
        }
    }

}