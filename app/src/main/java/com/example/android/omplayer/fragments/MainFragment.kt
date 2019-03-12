package com.example.android.omplayer.fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.android.omplayer.R
import com.example.android.omplayer.activities.MainActivity
import com.example.android.omplayer.di.SingletonHolder
import com.example.android.omplayer.entities.LibraryUtil
import com.example.android.omplayer.viewmodels.LibraryViewModel
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment(), View.OnClickListener {

    private val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity)
            .setActionBarTitle("OMPlayer")
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        val libraryViewModel = LibraryViewModel(SingletonHolder.application.applicationContext)

        if (checkPermissionREAD_EXTERNAL_STORAGE(context)) {
            libraryViewModel.loadDataToDb()
        }
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
                if (LibraryUtil.tracks.isNotEmpty()) {

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

    private fun checkPermissionREAD_EXTERNAL_STORAGE(
        context: Context?
    ): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        context as Activity,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    showDialog(
                        "External storage", context,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )

                } else {
                    ActivityCompat
                        .requestPermissions(
                            context,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                        )
                }
                return false
            } else {
                return true
            }

        } else {
            return true
        }
    }

    fun showDialog(
        msg: String, context: Context,
        permission: String
    ) {
        val alertBuilder = AlertDialog.Builder(context)
        alertBuilder.setCancelable(true)
        alertBuilder.setTitle("Permission necessary")
        alertBuilder.setMessage("$msg permission is necessary")
        alertBuilder.setPositiveButton(
            android.R.string.yes
        ) { dialog, which ->
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(permission),
                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
            )
        }
        val alert = alertBuilder.create()
        alert.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // do your stuff
            } else {
                Toast.makeText(
                    context, "GET_ACCOUNTS Denied",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> super.onRequestPermissionsResult(
                requestCode, permissions,
                grantResults
            )
        }
    }
}