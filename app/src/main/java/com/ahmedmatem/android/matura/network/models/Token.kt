package com.ahmedmatem.android.matura.network.models

import android.util.Log
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@Keep
@Entity(tableName = "token_table")
data class Token(
    @PrimaryKey @ColumnInfo(name = "user_name") val userName: String,
    @Json(name = "access_token") val token: String,
    @Json(name = "token_type") val type: String,
    @ColumnInfo(name = "expire_in") @Json(name = "expires_in") val expireIn: String,
    @ColumnInfo(name = "issued") @Json(name = ".issued") val issued: String,
    @ColumnInfo(name = "expires") @Json(name = ".expires") val expires: String,
    @ColumnInfo(name = "password") var password: String?
){
    fun isExpired(): Boolean {
        val dateFormat: SimpleDateFormat = SimpleDateFormat(DATE_FORMAT_PATTERN)
        var expires: Date? = null
        try {
            expires = dateFormat.parse(this.expires)
        } catch (exc: ParseException) {
            Log.e("", "isExpired: Error parsing token expires date format", null);
        }

        // Set calendar to date before day of token expiration
        val dayBeforeExpires = Calendar.getInstance()
        expires?.let {
            dayBeforeExpires.time = it
            dayBeforeExpires.add(Calendar.DATE, -1)
        }

        val now = Calendar.getInstance().time
        if (now.after(dayBeforeExpires.time)) {
            return true
        }
        return false
    }

    companion object {
        @Ignore const val DATE_FORMAT_PATTERN = "E, dd MMM yyyy HH:mm:ss"
    }
}

fun Token.withPassword(password: String?): Token {
    this.password = password
    return this
}