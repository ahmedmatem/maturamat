package com.ahmedmatem.android.matura.local.preferences

import android.content.Context
import android.content.SharedPreferences
import java.util.*

class UserPrefs(val context: Context) {

    data class User(
        var username: String,
        var password: String?,
        var token: String?
    )

    private val sharedPref: SharedPreferences by lazy {
        context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE)
    }

    fun setUser(username: String, password: String?, token: String?) {
        setUsername(username)
        setPassword(password)
        setToken(token)
    }

    /**
     * Use this function to detect if user has logged in its account or hasn't.
     * Null value means that user is not logged in and app is used from guest,
     * otherwise user has logged in.
     */
    fun getUser(): User? {
        getUsername()?.let {
            return User(it, getPassword(), getToken())
        }
        return null
    }

    fun isGuest(): Boolean {
        return !isUser()
    }

    fun isUser() : Boolean {
        return getUser() != null
    }

    fun logout() {
        setUsername(null)
    }

    private fun setUsername(username: String?) {
        with(sharedPref.edit()) {
            putString(USER_KEY, username)
            commit()
        }
    }

    private fun setPassword(password: String?) {
        with(sharedPref.edit()) {
            putString(PASSWORD_KEY, password)
            commit()
        }
    }

    private fun setToken(token: String?) {
        with(sharedPref.edit()) {
            putString(TOKEN, token)
            commit()
        }
    }

    private fun getUsername(): String? {
        return sharedPref.getString(USER_KEY, null)
    }

    private fun getPassword(): String? {
        return sharedPref.getString(PASSWORD_KEY, null)
    }

    private fun getToken(): String? {
        return sharedPref.getString(TOKEN, null)
    }

    /**
     * Use this method to obtain UUID from user preferences for guests.
     */
    fun getUuid(): String {
        var uuid = sharedPref.getString(UUID_KEY, null)
        return uuid ?: createUUID()
    }

    /**
     * Use this method only once to create UUID for guest and save it in user preferences.
     */
    private fun createUUID(): String {
        val uuid = UUID.randomUUID().toString()
        with(sharedPref.edit()) {
            putString(UUID_KEY, uuid)
            commit()
        }
        return uuid
    }

    /**
     * Setter for FCM registration token
     */
    fun setFcmToken(token: String?) {
        with(sharedPref.edit()) {
            putString(FCM_TOKEN_KEY, token)
            commit()
        }
    }

    /**
     * Getter for FCM registration token
     */
    fun getFcmToken(): String? {
        return sharedPref.getString(FCM_TOKEN_KEY, null)
    }

    companion object {
        const val USER_KEY = "user_key"
        const val PASSWORD_KEY = "password_key"
        const val TOKEN = "token"
        const val UUID_KEY = "uuid_key"
        const val FCM_TOKEN_KEY = "fcm_token_key"
        const val SHARED_PREFS_FILE = "shared_prefs_file"
    }
}