package com.ahmedmatem.android.matura.local.preferences

import android.content.Context
import android.content.SharedPreferences
import com.ahmedmatem.android.matura.R

class UserPrefs(val context: Context) {

    private val sharedPref: SharedPreferences by lazy {
        context.getSharedPreferences(
            context.getString(R.string.shared_pref_file_key),
            Context.MODE_PRIVATE
        )
    }

    // Hold the username of the logged in user.
    // Null value means no user logged in (the app is used by guest).
    fun setUser(username: String?) {
        with(sharedPref.edit()) {
            putString(context.getString(R.string.user_key), username)
            apply()
        }
    }

    fun getUser(): String? {
        return sharedPref.getString(context.getString(R.string.user_key), null)
    }
}