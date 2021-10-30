package com.ahmedmatem.android.matura.local.preferences

import android.content.Context
import android.content.SharedPreferences
import com.ahmedmatem.android.matura.R
import java.util.*

class UserPrefs(val context: Context) {

    private val sharedPref: SharedPreferences by lazy {
        context.getSharedPreferences(
            context.getString(R.string.shared_pref_file_key),
            Context.MODE_PRIVATE
        )
    }

    fun setUser(username: String?) {
        with(sharedPref.edit()) {
            putString(context.getString(R.string.user_key), username)
            apply()
        }
    }

    /**
     * Use this function to detect if user has logged in its account or hasn't.
     * Null value means that user is not logged in and app is used from guest,
     * otherwise user has logged in.
     */
    fun getUser(): String? {
        return sharedPref.getString(context.getString(R.string.user_key), null)
    }

    /**
     * Use this method to obtain UUID from user preferences for guests.
     */
    fun getUuid(): String {
        val uuidKey = context.getString(R.string.uuid_key)
        var uuid = sharedPref.getString(uuidKey, null)
        return uuid ?: createUUID()
    }

    /**
     * Use this method only once to create UUID for guest and save it in user preferences.
     */
    private fun createUUID(): String {
        val uuid = UUID.randomUUID().toString()
        with(sharedPref.edit()) {
            putString(context.getString(R.string.uuid_key), uuid)
            apply()
        }
        return uuid
    }
}