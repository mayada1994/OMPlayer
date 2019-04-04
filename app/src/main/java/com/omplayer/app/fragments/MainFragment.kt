package com.omplayer.app.fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.omplayer.app.R
import com.omplayer.app.activities.MainActivity
import com.omplayer.app.di.SingletonHolder
import com.omplayer.app.stateMachine.Action
import com.omplayer.app.utils.LibraryUtil
import com.omplayer.app.utils.PreferenceUtil
import com.omplayer.app.viewmodels.LibraryViewModel
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment(), View.OnClickListener {

    private val EXTERNAL_STORAGE_PERMISSIONS_REQUEST = 123
    lateinit var progressBar: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity)
            .setActionBarTitle(getString(R.string.app_name))
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        progressBar = view.findViewById(R.id.library_to_db_progress)
        val libraryViewModel = LibraryViewModel(SingletonHolder.application)

        if (checkPermissionREAD_EXTERNAL_STORAGE(context)) {
            if (libraryViewModel.emptyDb() || PreferenceUtil.updateLibrary) {
                progressBar.visibility = View.VISIBLE
                libraryViewModel.loadDataToDb(progressBar)
            } else {
                libraryViewModel.extractData()
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_player.setOnClickListener(this)
        btn_library.setOnClickListener(this)
        btn_favorites.setOnClickListener(this)
        
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_player -> {
                LibraryUtil.action = Action.Pause()
                view.findNavController().navigate(R.id.action_mainFragment_to_playerFragment)
            }

            R.id.btn_library -> {
                view.findNavController().navigate(R.id.action_mainFragment_to_libraryFragment)
            }
            R.id.btn_favorites -> {
                view.findNavController().navigate(R.id.action_mainFragment_to_favoritesFragment)
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
                            EXTERNAL_STORAGE_PERMISSIONS_REQUEST
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
                EXTERNAL_STORAGE_PERMISSIONS_REQUEST
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
            EXTERNAL_STORAGE_PERMISSIONS_REQUEST -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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