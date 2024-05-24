package net.onest.time.utils

import okhttp3.internal.notify

object StringUtil {
    @JvmStatic
    fun isEmail(email: String): Boolean {
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".toRegex())
    }

    @JvmStatic
    fun isPhone(phone: String): Boolean {
        return phone.matches("^1[3-9]\\d{9}$".toRegex())
    }
}
