package com.omplayer.app.utils

import java.text.DecimalFormat
import java.util.concurrent.TimeUnit.*

object FormatUtils {


    val decimalFormatDual = DecimalFormat("00")


    fun millisecondsToString (milliseconds : Long) : String {
        val hours = MILLISECONDS.toHours(milliseconds)
        val minutes = MILLISECONDS.toMinutes(milliseconds % HOURS.toMillis(1))
        val seconds = MILLISECONDS.toSeconds(milliseconds % MINUTES.toMillis(1))

        val builder = StringBuilder()
        if (hours != 0L) {
            builder.append(decimalFormatDual.format(hours)).append(':')
        }
        builder.append(decimalFormatDual.format(minutes)).append(':')
        builder.append(decimalFormatDual.format(seconds))

        return builder.toString()
    }
}