package com.ahmedmatem.android.matura.ui.account.login

data class LoginFormUiState(
    val username: String = "",
    val password: String = "",
    val enableLoginButton: Boolean = username.isNotBlank() && password.isNotBlank()
)
