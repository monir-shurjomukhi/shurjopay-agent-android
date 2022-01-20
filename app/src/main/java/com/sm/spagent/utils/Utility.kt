package com.sm.spagent.utils

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Utility {
    companion object {
        @JvmStatic
        fun rand(from: Int, to: Int): Int {
            val random = Random()
            return random.nextInt(to - from) + from
        }

        fun randId(): String {
            return randId(1111, 9999)
        }

        @SuppressLint("SimpleDateFormat")
        fun randId(from: Int, to: Int): String {
            val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("yyyymmdd")
            val calendarDate = Calendar.getInstance()
            val randVal = rand(from, to)
            return "${calendarDate.timeInMillis / 1000}${randVal.toString()}"
            //return "${simpleDateFormat.format(calendarDate.time)}${randVal.toString()}"
        }
    }

    fun getAbbreviatedFromDateTime(
        dateTime: String,
        dateFormat: String,
        field: String,
    ): String? {
        val input = SimpleDateFormat(dateFormat)
        val output = SimpleDateFormat(field)
        try {
            val getAbbreviate = input.parse(dateTime)    // parse input
            return output.format(getAbbreviate)    // format output
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return null
    }
}