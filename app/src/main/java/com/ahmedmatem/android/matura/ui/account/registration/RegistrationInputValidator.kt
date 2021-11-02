package com.ahmedmatem.android.matura.ui.account.registration

import com.ahmedmatem.android.matura.infrastructure.*
import java.util.regex.Pattern

class RegistrationInputValidator {
    private lateinit var username: String
    private lateinit var password: String
    private lateinit var passwordConfirm: String

    var errors = Error()

    fun isValid(un: String, p: String, pc: String): Boolean {
        validateInput(un, p, pc)
        return errors.value == 0
    }

    private fun validateInput(un: String, p: String, pc: String) {
        username = un
        password = p
        passwordConfirm = pc
        errors.value = 0 // initialize no error value
        validateEmail()
        validatePassword()
        validateConfirmPassword()
    }

    private fun validateEmail() {
        username?.let {
            if (it.isBlank()) {
                errors.add(Error.EMAIL_REQUIRED)
            } else {
                if (EMAIL_REGEX_PATTERN.matcher(username).matches()) {
                    errors.add(Error.EMAIL_INVALID_FORMAT)
                }
            }
        }
    }

    private fun validatePassword() {
        password?.let {
            if (it.isBlank()) {
                errors.add(Error.PASSWORD_REQUIRED)
            } else {
                // validate option by option
                if (PasswordOptions.REQUIRE_DIGIT && !it.hasDigit()) {
                    errors.add(Error.PASSWORD_DIGIT_REQUIRED)
                }
                if (PasswordOptions.REQUIRE_LOWERCASE && !it.hasLowerCase()) {
                    errors.add(Error.PASSWORD_LOWERCASE_REQUIRED)
                }
                if (PasswordOptions.REQUIRE_UPPERCASE && !it.hasUpperCase()) {
                    errors.add(Error.PASSWORD_UPPERCASE_REQUIRED)
                }
                if (PasswordOptions.REQUIRE_NON_ALPHANUMERIC && !it.hasNonAlphaNumeric()) {
                    errors.add(Error.PASSWORD_NON_ALPHANUMERIC_REQUIRED)
                }
                if (!it.hasLength(PasswordOptions.REQUIRED_LENGTH)) {
                    errors.add(Error.PASSWORD_REQUIRED_LENGTH)
                }
            }
        }
    }

    private fun validateConfirmPassword() {
        passwordConfirm?.let {
            if (it.isBlank()) {
                errors.add(Error.PASSWORD_CONFIRM_REQUIRED)
            } else {
                if (it != password) {
                    errors.add(Error.PASSWORDS_NO_MATCH)
                }
            }
        }
    }

    companion object {
        private val EMAIL_REGEX_PATTERN: Pattern =
            Pattern.compile("(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
    }
}

class Error {
    var value: Int = 0

    fun has(error: Int): Boolean {
        return (value and error) == error
    }

    fun add(error: Int) {
        value = value xor error
    }

    companion object {
        const val EMAIL_REQUIRED: Int = 0b0000000000000001 // 1
        const val EMAIL_INVALID_FORMAT: Int = 0b0000000000000010 // 2
        const val PASSWORD_REQUIRED: Int = 0b0000000000000100 // 4
        const val PASSWORD_DIGIT_REQUIRED = 0b0000000000001000 // 8
        const val PASSWORD_LOWERCASE_REQUIRED: Int = 0b0000000000010000 // 16
        const val PASSWORD_UPPERCASE_REQUIRED: Int = 0b0000000000100000 // 32
        const val PASSWORD_NON_ALPHANUMERIC_REQUIRED: Int = 0b0000000001000000 // 64
        const val PASSWORD_REQUIRED_LENGTH: Int = 0b0000000010000000 // 128
        const val PASSWORD_CONFIRM_REQUIRED: Int = 0b0000000100000000 // 256
        const val PASSWORDS_NO_MATCH: Int = 0b0000001000000000 // 512
    }
}