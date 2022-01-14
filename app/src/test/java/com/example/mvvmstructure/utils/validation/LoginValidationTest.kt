package com.example.mvvmstructure.utils.validation

import com.google.common.truth.Truth.assertThat
import org.junit.Test


class LoginValidationTest {

    @Test
    fun `empty email returns false`() {
        val result = LoginValidation.validate("", "123456")
        assertThat(result).isFalse()
    }

    @Test
    fun `empty password returns false`() {
        val result = LoginValidation.validate("yousefkazemy1@gmail.com", "")
        assertThat(result).isFalse()
    }

    @Test
    fun `incorrect email returns false`() {
        val result = LoginValidation.validate("yousefkazemy1@com", "123456")
        assertThat(result).isFalse()
    }

    @Test
    fun `less than 6 digits password returns false`() {
        val result = LoginValidation.validate("yousefkazemy1@gmail.com", "12345")
        assertThat(result).isFalse()
    }

    @Test
    fun `correct username and password return true`() {
        val result = LoginValidation.validate("yousefkazemy1@gmail.com", "123456")
        assertThat(result).isTrue()
    }
}