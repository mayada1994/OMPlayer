package com.omplayer.app.dialogFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.omplayer.app.R
import com.omplayer.app.activities.MainActivity
import com.omplayer.app.di.SingletonHolder
import com.omplayer.app.fragments.SettingsFragment
import com.omplayer.app.viewmodels.LastFmViewModel
import kotlinx.android.synthetic.main.last_fm_login_dialog.*

class LastFmLoginDialogFragment : DialogFragment(), View.OnClickListener {

    private val lastFmViewModel = LastFmViewModel(SingletonHolder.application)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.last_fm_login_dialog, container, false)
        dialog.setCanceledOnTouchOutside(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        login.setOnClickListener(this)
        register.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            login -> {
                val username = username.text.toString()
                val password = password.text.toString()
                lastFmViewModel.logIn(username, password, this@LastFmLoginDialogFragment, targetFragment as SettingsFragment)
            }

            register -> lastFmViewModel.register(activity as MainActivity)
        }
    }

    companion object {

        fun newInstance(fragment: SettingsFragment): LastFmLoginDialogFragment {
            val lastFmLoginDialogFragment = LastFmLoginDialogFragment()
            lastFmLoginDialogFragment.setStyle(STYLE_NO_TITLE, 0)
            lastFmLoginDialogFragment.setTargetFragment(fragment, 0)
            return lastFmLoginDialogFragment
        }
    }
}