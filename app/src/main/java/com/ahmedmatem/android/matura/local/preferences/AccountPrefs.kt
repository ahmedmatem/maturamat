package com.ahmedmatem.android.matura.local.preferences

import android.content.Context
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.ui.auth.login.LoginState

class AccountPrefs(val context: Context) {

    val sharedPref by lazy {
        context.getSharedPreferences(
            context.getString(R.string.shared_pref_file_key),
            Context.MODE_PRIVATE
        )
    }

    fun setUserLoginState(loginStatus: LoginState) {
        with(sharedPref.edit()) {
            when (loginStatus) {
                LoginState.IN -> putString(
                    context.getString(R.string.login_state_key),
                    LoginState.valueOf("IN").name
                )
                LoginState.OUT -> putString("login_state_key", LoginState.valueOf("OUT").name)
            }
            apply()
        }
    }

    fun getUserLoginState(): LoginState {
        return sharedPref.getString(
            context.getString(R.string.login_state_key),
            LoginState.valueOf("IN").name
        ) as LoginState
    }
}