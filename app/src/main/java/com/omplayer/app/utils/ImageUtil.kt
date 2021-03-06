package com.omplayer.app.utils

import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.omplayer.app.di.SingletonHolder
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object ImageUtil {

    fun saveImage(url: String, name: String) {
        Glide.with(SingletonHolder.application)
            .asBitmap()
            .load(url)
            .into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    write(name, resource)
                }
            })
    }

    private fun write(fileName: String, bitmap: Bitmap) {
        val path = SingletonHolder.application.filesDir.absolutePath
        val filePath = File(path, "$fileName.jpg")
        try {
            FileOutputStream(filePath).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}