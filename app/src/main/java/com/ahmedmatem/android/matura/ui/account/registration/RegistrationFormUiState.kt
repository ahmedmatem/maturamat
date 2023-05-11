package com.ahmedmatem.android.matura.ui.account.registration

data class RegistrationFormUiState(
    val showUsernameValidationMessage: Boolean = false,
    val usernameValidationMessage: String = "",
    val showPasswordValidationMessage: Boolean = false,
    val passwordValidationMessage: String = "",
    val showConfirmPasswordValidationMessage: Boolean = false,
    val confirmPasswordValidationMessage: String = "",
    val showFailMessage: Boolean = false,
    val failMessage: String = ""
)
