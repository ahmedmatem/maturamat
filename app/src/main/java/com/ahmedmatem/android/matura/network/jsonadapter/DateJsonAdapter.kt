package com.ahmedmatem.android.matura.network.jsonadapter

import android.util.Log
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateJsonAdapter {
    @FromJson
    fun dateFromJson(date: String): Date? {
        return try {
            synchronized(date) {
                dateFormat.parse(date)
            }
        } catch (pe: ParseException) {
            Log.d("DEBUG", "dateFromJson: Invalid date format: $date")
            null
        }
    }

    @ToJson
    fun dateToJson(date: Date): String {
        return synchronized(date) {
            dateFormat.format(date)
        }
    }

    companion object {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    }
}