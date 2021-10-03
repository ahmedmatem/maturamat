package com.ahmedmatem.android.matura.network.models

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Keep
@Entity(tableName = "token_table")
data class Token(
    @PrimaryKey @ColumnInfo(name = "user_name") val userName: String,
    @Json(name = "access_token") val token: String,
    @Json(name = "token_type") val type: String,
    @ColumnInfo(name = "expire_in") @Json(name = "expires_in") val expireIn: String,
    @ColumnInfo(name = "issued") @Json(name = ".issued") val issued: String,
    @ColumnInfo(name = "expires") @Json(name = ".expires") val expires: String
) {
    override fun toString(): String {
        return "AuthToken(token='$token', " +
                "type='$type', " +
                "expireIn='$expireIn', " +
                "userName='$userName', " +
                "issued='$issued', " +
                "expires='$expires')"
    }
}