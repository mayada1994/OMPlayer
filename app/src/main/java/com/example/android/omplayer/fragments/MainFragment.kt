package com.example.android.omplayer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.android.omplayer.R
import com.example.android.omplayer.activities.MainActivity
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity)
            .setActionBarTitle("OM Player")
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_player.setOnClickListener(this)
        btn_library.setOnClickListener(this)

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_player -> {
                fragmentManager?.apply {
                    beginTransaction()
                        .replace(
                            R.id.fragment_placeholder,
                            PlayerFragment()
                        )
                        .addToBackStack(null)
                        .commit()
                }
            }

            R.id.btn_library -> {
                fragmentManager?.apply {
                    beginTransaction()
                        .replace(
                            R.id.fragment_placeholder,
                            LibraryFragment()
                        )
                        .addToBackStack(null)
                        .commit()
                }
            }
        }
    }
}