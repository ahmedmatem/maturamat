package com.ahmedmatem.android.matura.ui.account.registration

import com.ahmedmatem.android.matura.infrastructure.*
import java.util.regex.Pattern

class RegistrationFormValidator {
    private var username: String? = null
    private var password: String? = null
    private var confirmPassword: String? = null

    var errors = Error()

    fun isValid(username: String, password: String, confirmPassword: String): Boolean {
        validateInput(username, password, confirmPassword)
        return errors.value == 0
    }

    fun isEmailValid(email: String): Boolean {
        validateInput(email, null, null)
        return errors.value == 0
    }

    private fun validateInput(un: String?, p: String?, cp: String?) {
        username = un
        password = p
        confirmPassword = cp
        errors.value = 0 // initialize no error value
        validateEmail()
        validatePassword()
        validateConfirmPassword()
    }

    private fun validateEmail() {
        username?.let {
            if (it.isEmpty()) {
                errors.add(Error.EMAIL_REQUIRED)
            } else {
                if (!EMAIL_REGEX_PATTERN.matcher(username).matches()) {
                    errors.add(Error.EMAIL_INVALID_FORMAT)
                }
            }
        }
    }

    private fun validatePassword() {
        password?.let {
            if (it.isEmpty()) {
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
                if (!it.hasLengthEqualOrGreaterThan(PasswordOptions.REQUIRED_LENGTH)) {
                    errors.add(Error.PASSWORD_REQUIRED_LENGTH)
                }
            }
        }
    }

    private fun validateConfirmPassword() {
        confirmPassword?.let {
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

        const val PASSWORD_GENERAL_VALIDATION_MESSAGE: Int = 0b0000000000000011 // 3

        val message: Map<Int,String> = mapOf(
            EMAIL_REQUIRED to "Изисква се имейл адрес",
            EMAIL_INVALID_FORMAT to "Невалиден имейл адрес",
            PASSWORD_REQUIRED to "Изисква се парола",
            PASSWORD_DIGIT_REQUIRED to "Паролата трябва да съдържа цифра",
            PASSWORD_LOWERCASE_REQUIRED to "Паролата трябва да съдържа малка буква",
            PASSWORD_UPPERCASE_REQUIRED to "Паролата трябва да съдържа главна буква",
            PASSWORD_NON_ALPHANUMERIC_REQUIRED to "Паролата трябва да съдържа специален символ",
            PASSWORD_REQUIRED_LENGTH to "Паролата трябва да е поне ${PasswordOptions.REQUIRED_LENGTH} символа",
            PASSWORD_CONFIRM_REQUIRED to "Изисква се потвърждение на паролата",
            PASSWORDS_NO_MATCH to "Паролите не съвпадат",
            PASSWORD_GENERAL_VALIDATION_MESSAGE to "Паролата трябва да съдържа поне ${PasswordOptions.REQUIRED_LENGTH} символа и малка буква"
        )
    }
}