package com.example.mvvmstructure.utils.validation

import java.util.regex.Pattern

object LoginValidation {

    private val EMAIL_ADDRESS = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
    )

    /**
     * The input is not valid if...
     * ...the email/password is empty
     * ...the email is not right format
     * ... the password is less than 6 digits
     */
    fun validate(
        email: String = "",
        password: String = ""
    ): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            return false
        }
        if (!EMAIL_ADDRESS.matcher(email).matches()) {
            return false
        }
        if (password.length < 6) {
            return false
        }
        return true
    }
}