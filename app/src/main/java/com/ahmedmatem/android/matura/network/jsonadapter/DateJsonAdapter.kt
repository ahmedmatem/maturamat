package com.ahmedmatem.android.matura.network.jsonadapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat
import java.util.*

class DateJsonAdapter {
    @FromJson
    fun dateFromJson(date: String): Date {
        return dateFormat.parse(date)
    }

    @ToJson
    fun dateToJson(date: Date): String {
        return dateFormat.format(date)
    }

    companion object {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    }
}