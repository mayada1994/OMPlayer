package com.example.android.omplayer.fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.android.omplayer.R
import com.example.android.omplayer.adapters.ArtistAdapter
import com.example.android.omplayer.db.entities.Artist


var artists = ArrayList<Artist>()

class ArtistFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_artist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL


        if (checkPermissionREAD_EXTERNAL_STORAGE(context)) {
            artists = scanDeviceForArtists()
        }

        Log.i("TAG!!!", artists.size.toString())

        val itemAdapter = ArtistAdapter(artists)

        val userList = activity!!.findViewById<RecyclerView>(R.id.artist_list_recycler_view)
        userList.layoutManager = layoutManager
        userList.adapter = itemAdapter
    }

    private fun scanDeviceForArtists(): ArrayList<Artist> {
        val where: String? = null


        val columns = arrayOf(
            MediaStore.Audio.Artists.ARTIST
        )
        val sortOrder =
            MediaStore.Audio.Artists.ARTIST + " COLLATE LOCALIZED ASC"

        var cursor: Cursor? = null
        try {
            val uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI
            cursor = context!!.contentResolver.query(uri, columns, where, null, sortOrder)
            if (cursor != null) {
                cursor.moveToFirst()
                var i = 0
                while (!cursor.isAfterLast) {
                    val name = if (cursor.getString(0) != null) cursor.getString(0) else "Unknown"

                    cursor.moveToNext()
                    artists.add(Artist(-1, name))

                }

            }

            // print to see list of mp3 files
            for (file in artists) {
                Log.i("TAG!!!", file.toString())
            }

        } catch (e: Exception) {
            Log.e("TAG!!!", e.toString())
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
        return artists
    }

    val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123

    fun checkPermissionREAD_EXTERNAL_STORAGE(
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