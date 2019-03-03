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
import com.example.android.omplayer.adapters.TrackAdapter
import com.example.android.omplayer.db.entities.Track
import com.example.android.omplayer.entities.LibraryUtil


lateinit var tracks: List<Track>

class TrackFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_track, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL


        if (checkPermissionREAD_EXTERNAL_STORAGE(context)) {
            tracks = scanDeviceForMp3Files()
        }

        Log.i("TAG!!!", tracks.size.toString())

        val itemAdapter = TrackAdapter(tracks)

        val userList = activity!!.findViewById<RecyclerView>(R.id.track_list_recycler_view)
        userList.layoutManager = layoutManager
        userList.adapter = itemAdapter
    }

    private fun scanDeviceForMp3Files(): List<Track> {
        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
        val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION
        )
        val sortOrder =
            MediaStore.Audio.AudioColumns.ARTIST + "," + MediaStore.Audio.AudioColumns.ALBUM + " COLLATE LOCALIZED ASC"
        val mp3Files = ArrayList<Track>()

        var cursor: Cursor? = null
        try {
            val uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            cursor = activity!!.contentResolver.query(uri, projection, selection, null, sortOrder)
            if (cursor != null) {
                cursor.moveToFirst()
                var i = 0
                while (!cursor.isAfterLast) {
                    val title = cursor.getString(0)
                    val artist = cursor.getString(1)
                    val path = cursor.getString(2)
                    val displayName = cursor.getString(3)
                    val songDuration = cursor.getString(4)
                    cursor.moveToNext()
                    if (path != null && path.endsWith(".mp3")) {
                        mp3Files.add(Track(i++, title, "", songDuration.toInt(), 0, path))
                    }
                }

            }

            // print to see list of mp3 files
            for (file in mp3Files) {
                LibraryUtil.tracklist.add(file.path)
            }

        } catch (e: Exception) {
            Log.e("TAG!!!", e.toString())
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
        return mp3Files
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
        alertBuilder.setPositiveButton(android.R.string.yes
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