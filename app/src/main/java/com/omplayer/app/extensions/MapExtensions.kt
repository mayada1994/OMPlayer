package com.omplayer.app.extensions


import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

fun Map<String, Any>.toBundle(): Bundle {
    val bundle = Bundle()
    forEach {
        it.value.let { value ->
            when (value) {
                is Boolean -> bundle.putBoolean(it.key, value)
                is Byte -> bundle.putByte(it.key, value)
                is Char -> bundle.putChar(it.key, value)
                is Short -> bundle.putShort(it.key, value)
                is Int -> bundle.putInt(it.key, value)
                is Long -> bundle.putLong(it.key, value)
                is Float -> bundle.putFloat(it.key, value)
                is Double -> bundle.putDouble(it.key, value)
                is String -> bundle.putString(it.key, value)
                is CharSequence -> bundle.putCharSequence(it.key, value)
                is Parcelable -> bundle.putParcelable(it.key, value)
                is Serializable -> bundle.putSerializable(it.key, value)
                else -> {
                }
            }
        }
    }
    return bundle
}